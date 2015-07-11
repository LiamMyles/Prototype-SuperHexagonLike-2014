//This subclass handles mainly all the methods related to the interaction between the
//laser and the shapes. This class is basically the same as ClassicMode and has more detailed comments
//than ClassicMode. The reason a lot of code is duplicated is because I didn't have time to figure
//out a way to pass an arraylist of a different type to a method. 
class FireAndIceMode extends GameMethods
{
  //Two arrays to enable an infinite loop
  ArrayList<FireAndIceRings> firePattern1;
  ArrayList<FireAndIceRings> firePattern2;
  //Other variables used in this subclass
  boolean shotFired;
  int arrayChoice;
  int firePatternSize;
  
  //Constructor that sets the default values
  FireAndIceMode()
  {
    shotFired = false;
    arrayChoice = 1;
    firePatternSize = 10;//Size of the pattern arrays
  }

  ///////////////////////////Shape Drawing///////////////////////////
  ///////////////////////////Shape Drawing///////////////////////////
  ///////////////////////////Shape Drawing///////////////////////////

  //Draws all the relevant methods to the screen
  void drawToScreen()
  {
    
    //ArrayChoice gets changed by an outside function, this is to cre*****
    if (arrayChoice == 1)
    {
      drawShapes(firePattern1);
    }
    else if (arrayChoice == 2)
    {
      drawShapes(firePattern2);
    }
    lazar();
    controlGui();
    screen();
  }

  void pickLazarColour()
  {
    if (lazarElementFire == true  && lazarOn == true)
    {
      lazarColour = color(247, 196, 25);
    } 
    else if (lazarElementFire == false && lazarOn == true)
    {
      lazarColour = color(64, 247, 255);
    }
  }


  void controlGui()
  {
    //bottom control area
    rect(0, height*0.766, width, height*0.234);//Changed to percent
    //button area
    rect(width*0.292, height*0.797, width*0.416, height*0.156);//Changed to percent
    //Ice Laser
    ellipse(width*0.166, height*0.875, width*0.166, height*0.094);//Changed to percent
    //Fire Laser
    ellipse(width*0.833, height*0.875, width*0.166, height*0.094);//Changed to percent
    //left button
    rect(width*0.292, height*0.797, width*0.208, height*0.156);//Changed to percent
    //right button 
    rect(width/2, height*0.797, width*0.208, height*0.156);//Changed to percent
    shapeMode(RIGHT);
    shape(fireUI,0,0,width,height);
    shapeMode(CENTER);
  }

  void createRings(int level)
  {
    int level1, level2;
    if (level > 2)
    {
      level1 = 3;
      level2 = 3;
    }
    else
    {
      level1 = level;
      level2 = level+1;
    }
    //number of rings in the pattern
    //Creating an array list of Instances of the FireAndIceMode class
    firePattern1 = new ArrayList<FireAndIceRings>(firePatternSize);
    firePattern2 = new ArrayList<FireAndIceRings>(firePatternSize);
    //a forloop that fills the arrayList with instances, first value is delay in milsec
    //the second is asking if the shape is rotatioing clockwise. 
    for (int i = 0; i < firePatternSize; i++)
    {
      int ran = floor(random(2));
      if (ran == 0)
      {
        firePattern1.add(new FireAndIceRings(i*2000, false, floor(random(6,11)), 2+level1, height*0.00078+(level1*(height*0.00078)), true ));//Changed to percent
      }
      else if (ran == 1)
      {
        firePattern1.add(new FireAndIceRings(i*2000, true, floor(random(6,11)), 2+level1, height*0.00078+(level1*(height*0.00078)), false ) );//Changed to percent
      }
    }

    for (int i = 0; i < firePatternSize; i++)
    {  
      int ran = floor(random(2));
      if (ran == 0)
      {
        firePattern2.add(new FireAndIceRings(i*2000, false, floor(random(6,11)), 2+level2, height*0.00078+(level2*(height*0.00078)), true ) );//Changed to percent
      }
      else if (ran == 1)
      {
        firePattern2.add(new FireAndIceRings(i*2000, true, floor(random(6,11)), 2+level2, height*0.00078+(level2*(height*0.00078)), false ) );//Changed to percent
      }
    }
  }

  void drawShapes(ArrayList<FireAndIceRings> array)
  {
    int arrayOfRadius[] = new int[array.size()];

    for (int i = 0; i < array.size(); i++)
    {
      FireAndIceRings shape = array.get(i);

      if (shape.radius == 0 || shape.shapeDestroyed == true)
      {
        arrayOfRadius[i] = round(height*0.781);//Changed to percent
        continue;
      }
      arrayOfRadius[i] = shape.radius;
      shape.drawShape();
    } 
    distFromRing = min(arrayOfRadius) - height*0.125;//Changed to percent



    for ( int i = 0; i < array.size(); i++)
    {
      FireAndIceRings shape = array.get(i);
      if (arrayOfRadius[i] == min(arrayOfRadius))
      {
        shape.detectionOn = true;
      } 
      else
      {
        shape.detectionOn = false;
      }
    }
  }


  void stopLazar(ArrayList<FireAndIceRings> array)
  {
    for (int i = 0; i < array.size(); i++)
    {
      FireAndIceRings shape = array.get(i);

      if (shape.lazarHit == true)
      {
        lazarVisable = false;
        lazarOn = false;
        lazarHasBeenShot = false;
      }
    }

    for (int i = 0; i < array.size(); i++)
    {
      FireAndIceRings shape = array.get(i);
      shape.lazarHit = false;
      lazarHasBeenShot = true;
    }
  }

  void fireShipGun()
  {
    //Used to delay the shoot function and stop the laser from showing for too long
    if (shotFired == false)
    {
      timeFired = millis();
      shotFired = true;
      lazarOn = true;
      lazarVisable = true;
      shipColour = color(211, 60, 30);
      shipReady = loadShape("Ship-Cooldown.svg");
      pickLazarColour();
    }
  }
  //Draws the laser beam and makes sure its orientated the right way. 
  void lazar()
  {
    if (lazarOn == true || lazarVisable == true)
    {
      pushMatrix();
      translate(width/2, height*0.383);//Changed to percent
      rotate(radians(shipAngleX));
      fill(lazarColour);
      rect(lazarX-(width*0.008),lazarY-(height*0.005),distFromRing, height*0.008);
      rectMode(CORNER);
      popMatrix();

      if (millis() < timeFired+300)
      {
        lazarVisable = true;
        lazarOn = true;
      } 
      else if (millis() < timeFired+300 || lazarHasBeenShot == true)
      {
        lazarVisable = false;
        lazarOn = false;
      }
      if (millis() > timeFired+10)
      {
        lazarOn = false;
      }
      if (arrayChoice == 1)
      {
        stopLazar(firePattern1);
      }
      else if (arrayChoice == 2)
      {
        stopLazar(firePattern2);
      }
    }
    if (millis() > timeFired+1000)
    {
      shotFired = false;
      shipColour = color(43, 207, 211);
      shipReady = loadShape("Ship-Ready.svg");
    }
    fill(255);
  }
}

