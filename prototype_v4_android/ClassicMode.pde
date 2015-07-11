//This subclass handles mainly all the methods related to the interaction between the
//laser and the shapes. This class is basically the same as ClassicMode and has more detailed comments
//than ClassicMode. The reason a lot of code is duplicated is because I didn't have time to figure
//out a way to pass an arraylist of a different type to a method. 
class ClassicMode extends GameMethods
{
  //Two arrays to enable an infinite loop
  ArrayList<RingCreation> pattern1;
  ArrayList<RingCreation> pattern2;
  //Other variables used in this subclass
  boolean shotFired;
  int arrayChoice;
  int patternSize;

  //Constructor that sets the default values
  ClassicMode()
  {
    shotFired = false;
    arrayChoice = 1;
    patternSize = 10;//Size of the pattern arrays
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
      drawShapes(pattern1);
    }
    else if (arrayChoice == 2)
    {
      drawShapes(pattern2);
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
      lazarColour = color(206, 44, 44);
    }
  }


  void controlGui()
  {
    //bottom control area
    rect(0, height*0.766, width, height*0.234);//Changed to percent
    //button area
    rect(width*0.222, height*0.797, width*0.555, height*0.156);//Changed to percent
    //fire button
    ellipse(width/2, height*0.875, height*0.094, height*0.094);//Changed to percent
    //left button
    rect(width*0.222, height*0.797, width*0.18, height*0.156);//Changed to percent
    //right button 
    rect(width*0.597, height*0.797, width*0.18, height*0.156);//Changed to percent /
    imageMode(CORNER);
    image(classicUI,0,0,width,height);
    imageMode(CENTER);
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
    pattern1 = new ArrayList<RingCreation>(patternSize);
    pattern2 = new ArrayList<RingCreation>(patternSize);
    //a forloop that fills the arrayList with instances, first value is delay in milsec
    //the second is asking if the shape is rotatioing clockwise. 
    for (int i = 0; i < patternSize; i++)
    {
      int ran = floor(random(2));
      if (ran == 0)
      {
        pattern1.add(new RingCreation(i*2000, false, floor(random(6, 11)), 2+level1, height*0.00078+(level1*(height*0.00078)) ));//Changed to percent
      }
      else if (ran == 1)
      {
        pattern1.add(new RingCreation(i*2000, true, floor(random(6, 11)), 2+level1, height*0.00078+(level1*(height*0.00078)) ) );//Changed to percent
      }
    }

    for (int i = 0; i < patternSize; i++)
    {  
      int ran = floor(random(2));
      if (ran == 0)
      {
        pattern2.add(new RingCreation(i*2000, false, floor(random(6, 11)), 2+level2, height*0.00078+(level2*(height*0.00078)) ) );//Changed to percent
      }
      else if (ran == 1)
      {
        pattern2.add(new RingCreation(i*2000, true, floor(random(6, 11)), 2+level2, height*0.00078+(level2*(height*0.00078)) ) );//Changed to percent
      }
    }
  }

  void drawShapes(ArrayList<RingCreation> array)
  {
    int arrayOfRadius[] = new int[array.size()];

    for (int i = 0; i < array.size(); i++)
    {
      RingCreation shape = array.get(i);

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
      RingCreation shape = array.get(i);
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


  void stopLazar(ArrayList<RingCreation> array)
  {
    for (int i = 0; i < array.size(); i++)
    {
      RingCreation shape = array.get(i);

      if (shape.lazarHit == true)
      {
        lazarVisable = false;
        lazarOn = false;
        lazarHasBeenShot = false;
      }
    }

    for (int i = 0; i < array.size(); i++)
    {
      RingCreation shape = array.get(i);
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
      shipReady = loadImage("Ship-Cooldown.png");
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
      rect(lazarX-(width*0.008), lazarY-(height*0.005), distFromRing, height*0.008);
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
        stopLazar(pattern1);
      }
      else if (arrayChoice == 2)
      {
        stopLazar(pattern2);
      }
    }
    if (millis() > timeFired+1000)
    {
      shotFired = false;
      shipColour = color(43, 207, 211);
      shipReady = loadImage("Ship-Ready.png");
    }
    fill(255);
  }
}

