///////////////////////////Classes///////////////////////////
///////////////////////////Classes///////////////////////////
///////////////////////////Classes///////////////////////////

class RingCreation
{
  float rScaleRate, rotateSpeed, rotateSpeedN;
  color weakPointColour;
  int shapeState, sideSelection, angleL, angleH, delayStart, delayDuration, radius, rotation, shapeSides, shapeSidesAngle, lastNumber;
  boolean clockwise, shapeOn, delay, startShape, degreePicked, shapeDestroyed, detectionOn, lazarHit, fireElement;
  //line
  float line1X, line1Y, line2X, line2Y;

  //Random weak point constructor with delay input
  //Parameters: Delay 1000 = 1 second, Clockwise? true = yes, how many sides? 6 minimum, rotation Speed, Scale Rate. 
  RingCreation(int delayD, boolean cW, int sides, int rS, float sR)
  {
    //default boolean states. 
    lazarHit = false;
    shapeOn = true;
    delay= false;
    degreePicked = false;
    shapeDestroyed = false;
    
    radius = width;//Size the shape starts at.
    rScaleRate = sR;//rate it scales at
    shapeState = 0;//The state the ring is in; 0 = moving, 1 = Destroyed Ship, 3 = Ring Destroyed
    rotateSpeed = rS;//speed the shape rotates at.
    rotation = 0;//start state.
    clockwise = cW;//Boolean for rotation direction 
    delayDuration = delayD;//delay for when the next shape is drawn
    weakPointColour = color(206, 44, 44);//colour of the weak point.
    
    //takes the sides parameter and then calculates the angle required for its hit area.
    shapeSides = sides;
    shapeSidesAngle = 360/shapeSides;
    
    sideSelection = floor(random(shapeSides));//randomly picks the weak point side. 
  }
  
  //Function that draws the shape
  //the shape is draw if in shapeState 0, state 1 and 2 will turn off the entire code. 
  void drawShape() 
  { 
    println(rotateSpeed);
    //function that adds a delay based on the parameters given.
    if (shapeActive() == true && shapeState == 0)
    {  
      //decides how the shape rotates
      rotation = rotationDirection(rotation);
      //draws the hex and the red line
      drawHex();
      //decides the angle based on the side selected for collison area
      angleSelection();
      //reduces the size of the ring based on the rScaleRate
      radius -= rScaleRate;
      //checks if the laser has hit the weak point
      if (lazarHitDetect() == true)
      {
        shapeState = 2;
      } 
      //Ring has reached the ship 
      if (radius < height*0.133)//Changed to percent
      {
        //changes the shapes state to 1 which means the ship has been hit
        shapeState = 1;
      }
    }
    else if (shapeState == 1)
    {
      shapeState = 4;
      shapeOn = false;
      playerLives -= 1;
      radius = 0;
    }
    else if (shapeState == 2)
    {
      shapeState = 4;
      shapeDestroyed = true;
      shapeOn = false;
      lazarHit = true;
      playerScore += 100;
      firePlayerScore += 100;
    }
  }


  //method that delays the ring basied on the input given
  boolean delay()
  {
    if (delay == false)
    {
      delayStart = millis();
      delay = true;
      return true;
    }
    if (delayStart+delayDuration <= millis())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  int rotationDirection(int input)
  {
    //decides the direction of the shape
    if (clockwise == true)
    {
      input += rotateSpeed;
    }
    else if (clockwise == false)
    {
      input -= rotateSpeed;
    }
    //if the rotation goes beyond 360 degrees this will reset it
    input = dont360(input);
    return input;
  }

  //Stops the rotation from going beyond 360 degrees.
  int dont360(int degree)
  {
    if (degree >= 360)
    {
      degree = 1;
    }
    else if (degree <= 0)
    {
      degree = 359;
    }
    return degree;
  }


  //Checks if the shape should be running
  boolean shapeActive()
  {
    if (shapeOn == true && delay() == true)
    {
      return true;
    }
    return false;
  }


  //Draws the shape with the amount of sides specified by the contructor.
  void drawHex()
  {
    //starts pushMatrix so shape can rotate   
    pushMatrix();
    //Places in centre of game area
    translate(width/2, height*0.383);//Changed to percent
    //rotates the shape depending on the degrees of the rotation variable
    rotate(radians(rotation));
    fill(0, 0);
    beginShape();
    //Loop that draws a hexagon, it also finds the points for the selected weak side.
    for (int i = 0; i <= shapeSides; i++) 
    {
      strokeWeight(floor(height*0.0047));
      stroke(60, 31, 124);
      float angle = radians(shapeSidesAngle)*i;
      vertex(cos(angle) * radius, sin(angle) * radius);

      if (i == sideSelection)
      {
        line2X = cos(angle) * radius;
        line2Y = sin(angle) * radius;
      }
      else if (i == sideSelection+1 )
      {
        line1X = cos(angle) * radius;
        line1Y = sin(angle) * radius;
      }
    }
    //finishes the shape.
    endShape(CLOSE); 
    fill(255);

    //draws the line basied on the vales given in the for loop
    stroke(weakPointColour);
    line(line1X, line1Y, line2X, line2Y);
    stroke(0);
    strokeWeight(floor(height*0.0016));
    popMatrix();
  }

  boolean lazarHitDetect()
  {
    //huge if statement to check if the angle of the ship is in line with the angle of the shapes weakpoint
    //It is quite long to check for the weird angles between 59 and 359.
    if ( ( (shipAngle > angleL) &&  (shipAngle < angleH) )  || 
      ( (angleH < shapeSidesAngle-1) && (shipAngle < angleH) && (shipAngle > 1) ) || 
      ( (angleL > 361-shapeSidesAngle) && (shipAngle < 360) && (shipAngle > angleL) ) )
    {
      if (lazarState == true && detectionOn == true)
      {
        return true;
      }
    }
    return false;
  }


  //Assigns the two angle points of the weak side
  //If the degree has already been picked it will perform the calculations
  //so that the angle can be rotated.
  void angleSelection()
  {
    if (degreePicked == false)
    {
      for (int i = 0; i < shapeSides; i++)
      {
        if (sideSelection == i)
        {
          angleL = (i*shapeSidesAngle);
          angleH = (angleL + shapeSidesAngle);
          degreePicked = true;
          break;
        }
      }
    }
    if (degreePicked == true);
    {
      angleL = rotationDirection(angleL);
      if (angleL > 360-shapeSidesAngle)
      {
        angleH = angleL + shapeSidesAngle - 359;
      }
      else
      {
        angleH = (angleL + shapeSidesAngle);
      }
    }
  }
}

//Subclass of RingCreation which is used to change the rings to work with the FireAndIceMode game mode.
class FireAndIceRings extends RingCreation 
{
  boolean fireOn;
  //Takes the constructor from RingCreation and adds a boolean to decide if the weak
  //point of the ring is fire or ice.
  FireAndIceRings(int delay, boolean cW, int sides, int rS, float sR, boolean fire)
  {
    super(delay, cW, sides, rS, sR);
    fireOn = fire;
  }

  //Decides the colour of the laser
  void pickColour()
  {
    if (fireOn == true)
    {
      weakPointColour = color(227, 137, 2);
    }
    else
    {
      weakPointColour = color(64, 247, 255);
    }
  }

  //Method that is called to draw eveything that is required to be on the screen.
  void drawShape() 
  { 
    pickColour();
    //Calls the ringcreation drawShapes method using super. to prevent conflicts
    super.drawShape();
  }

  //LazarHitDetect overrides the LazarHitDetect of the SuperClass to prevent complication. 
  //By doing this it allows the shape to detect what type of laser is hitting it. 
  boolean lazarHitDetect()
  {

    if ( ( (shipAngle > angleL) &&  (shipAngle < angleH) )  || 
      ( (angleH < 59) && (shipAngle < angleH) && (shipAngle > 1) ) || 
      ( (angleL > 301) && (shipAngle < 360) && (shipAngle > angleL) ) )
    {
      if (lazarState == true && detectionOn == true && fireOn == lazarElementFire)
      {
        return true;
      }
    }
    return false;
  }
}

