package processing.test.prototype_v4_android;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class prototype_v4_android extends PApplet {

////////////////////////////////////Global Variables////////////////////////////////////
////////////////////////////////////Global Variables////////////////////////////////////
////////////////////////////////////Global Variables////////////////////////////////////

boolean mouseDown, fireMode, updatedHighscores,requestInfo;
int shipAngle;
PImage planetImg;
PImage shipReady;
PImage classicUI;
PImage fireUI;
PImage gameoverUI;
PImage highscoreUI;
PImage mainmenuUI;
PImage trainUI;

//highscore Related
int playerScore;
int firePlayerScore;
//arrays for sorting and storing highscores
int[] scoreListOld;
int[] scoreListNew;
int[] fireScoreListOld;
int[] fireScoreListNew;

//lazar related globals
boolean lazarState, lazarElementFire;

//score related globals  
//int playerScore;
int playerLives;

//Screen Related globals
int appScreen;

// ring related
boolean levelUp;
int ringLevel;

//instances Globals, each are extended from the class GameMethods
ClassicMode classicScreen;
FireAndIceMode fireScreen;

////////////////////////////////////Setup////////////////////////////////////
////////////////////////////////////Setup////////////////////////////////////
////////////////////////////////////Setup////////////////////////////////////


public void setup()
{
  //basic setup loop for background, screen size, text size and framerate.
  background(255);
  //size(360, 640);
  frameRate(30);
  smooth();
  textSize(floor(height*0.05f));

  //Pre loads the graphics
  planetImg = loadImage("planet.png");
  shipReady = loadImage("Ship-Ready.png");
  mainmenuUI = loadImage("mainMenu_ui.png");
  gameoverUI = loadImage("gameover_ui.png");
  highscoreUI = loadImage("highScore_ui.png");
  classicUI = loadImage("classic_ui.png");
  fireUI = loadImage("fire_ui.png");
  trainUI = loadImage("train_ui.png");
  
  //calls the loadhighscores function so it populates the arrays with itens.
  //allows the user to check thire highscore before playing a game
  loadHighscores();

  //Boolean setups
  updatedHighscores = false;
  levelUp = false;
  mouseDown = false;

  //Default values for variables
  playerScore = 0;
  playerLives = 3;
  ringLevel = 0;
  appScreen = 0;

  //shape related
  //Initialises the objects
  fireScreen = new FireAndIceMode();
  classicScreen = new ClassicMode();
}

////////////////////////////////////Draw////////////////////////////////////
////////////////////////////////////Draw////////////////////////////////////
////////////////////////////////////Draw////////////////////////////////////


public void draw()
{
  background(255);
  fill(0);
  //Draws the screen depending on the appScreen Variable. 
  // 0 = main menu, 1 = classic game, 2 = fire game, 3 = game over, 4 = highscores
  screenDisplay();
  //Checks the mouse detection based on the screen the user is on
  mouseControls();
}

////////////////////////////////////GUI////////////////////////////////////
////////////////////////////////////GUI////////////////////////////////////
////////////////////////////////////GUI////////////////////////////////////
//Function to draw the main menu.
public void mainMenuGUI()
{
  fill(255);
  //rect(width*0.222, height*0.234, width*0.555, height*0.078);//Changed to percent
  fill(211, 147, 6);
  //rect(width*0.222, height*0.344, width*0.555, height*0.078);//Changed to percent
  //rect(width*0.222, height*0.453, width*0.555, height*0.078);//Changed to percent
  //ellipse(width*0.903, height*0.055, height*0.055, height*0.055);
  imageMode(CORNER);
  image(mainmenuUI,0,0,width,height);
  imageMode(CENTER);
}

//Draws the highscores when called.
public void highscoreGUI()
{
  rect(width*0.222f, height*0.81f, width*0.555f, height*0.078f);//Changed to percent
  imageMode(CORNER);
  image(highscoreUI,0,0,width*0.99999f,height*0.99999f);
  imageMode(CENTER);
}

//Draws the game over GUI
public void gameOverGUI()
{
  //Displays the game over screen
  textSize(floor(height*0.0781f));
  textAlign(CENTER);
  text("Game Over", width/2, height*0.383f);//Changed to percent
  textSize(floor(height*0.0469f));
  if (fireMode == true)
  {
    text("You scored "+firePlayerScore, width/2, height*0.461f);//Changed to percent
  }
  else if (fireMode == false)
  {
    text("You scored "+playerScore, width/2, height*0.461f);
  }
  
  rect(width*0.222f, height*0.687f, width*0.555f, height*0.078f);//Changed to percent
  rect(width*0.222f, height*0.578f, width*0.555f, height*0.078f);//Changed to percent
  imageMode(CORNER);
  image(gameoverUI,0,0,width,height);
  imageMode(CENTER);
}
////////////////////////////////////CONTROLS////////////////////////////////////
////////////////////////////////////CONTROLS////////////////////////////////////
////////////////////////////////////CONTROLS////////////////////////////////////

//Keyboard controls used for testing.
public void keyReleased()
{
  keyCode = BACKSPACE;

  if (key == ENTER || key == RETURN)
  {
    //gameReset();
  }
  if (key == '1')
  {
    appScreen = 1;
    classicScreen.createRings(ringLevel);
  }
  if (key == '2')
  {
    appScreen = 2;
    fireScreen.createRings(ringLevel);
  }
  if (key == '3')
  {
    appScreen = 3;
  }
  if (key == '4')
  {
    appScreen = 4;
  }
  if (key == '0')
  {
    appScreen = 0;
  }
}

//Have a boolean turn either true or false based on mousePressed and mouseReleased
//This is to let the app know when the user is holding the mouse down
public void mousePressed()
{
  mouseDown = true;
  //println(mouseDown);
}

public void mouseReleased()
{
  mouseDown = false;
  requestInfo = false;
  //println(mouseDown);
}


//Function thats runs when mouse is held down, otherwise it does nothing.
//This is used for the 'hit' areas of all the UI buttons
public void mouseControls()
{
  if (mouseDown == true)
  {
    if (appScreen == 0)//Starting/main menu
    {
      //Launches classic game mode and creates the rings inside the ClassicMode subclass
      if ( (mouseX > width*0.222f) && (mouseY > height*0.234f) && (mouseX < width*0.777f) && (mouseY < height*0.31f))//Changed to percent
      {
        appScreen = 1;
        classicScreen.createRings(ringLevel);
      }
      //Launches fire and ice game mode and creates the rings inside the FireAndIceMode class
      if ( (mouseX > width*0.222f) && (mouseY > height*0.344f) && (mouseX < width*0.777f) && (mouseY < height*0.422f))//Changed to percent
      {
        appScreen = 2;
        fireScreen.createRings(ringLevel);
      }
      //Launches the high scores page
      if ( (mouseX > width*0.222f) && (mouseY > height*0.453f) && (mouseX < width*0.777f) && (mouseY < height*0.53f))//Changed to percent
      {
        appScreen = 4;
      }
      if (dist(width*0.903f, height*0.055f, mouseX, mouseY) < height*0.055f/2)//Changed to percent
      {
        if (requestInfo == true)
        {
          requestInfo = false;
        }
        else
        {
          requestInfo = true;
          mouseDown = false;
        }

      }
    }

    else if (appScreen == 1)//classic game mode screen
    {  
      //Moves the ClassicMode ship left
      if ( (mouseX > width*0.222f) && (mouseY > height*0.797f) && (mouseX < width*0.403f) && (mouseY < height*0.953f) )//Changed to percent
      {
        classicScreen.moveShipLeft();
      }
      //Moves the ClassicMode ship right
      if ((mouseX > width*0.597f) && (mouseY > height*0.797f) && (mouseX < width*0.777f) && (mouseY < height*0.953f) )//Changed to percent
      {
        classicScreen.moveShipRight();
      }
      //Will shoot the ClassicMode ship gun/laser
      if (dist(width*0.50f, height*0.875f, mouseX, mouseY) < height*0.047f)//Changed to percent
      {
        classicScreen.fireShipGun();
      }
    }
    else if (appScreen == 2)//Fire and Ice GameMode Screen
    {
      //Moves the FireAndIceMode ship left
      if ( (mouseX > width*0.292f) && (mouseY > height*0.797f) && (mouseX < width*0.50f) && (mouseY < height*0.953f) )//Changed to percent
      {
        fireScreen.moveShipLeft();
      }
      //Moves the FireAndIceMode ship right
      if ((mouseX > width*0.50f) && (mouseY > height*0.797f) && (mouseX < width*0.708f) && (mouseY < height*0.953f) )//Changed to percent
      {
        fireScreen.moveShipRight();
      }
      //Fires the FireAndIceMode ship gun and tells the app the the laser is 'fire'
      if (dist(width*0.166f, height*0.875f, mouseX, mouseY) < height*0.047f)//Changed to percent
      {
        lazarElementFire = true;//Decides the laser type; Fire or Ice
        fireScreen.fireShipGun();
      }
      //Fires the FireAndIceMode ship gun and tells the app the the laser is 'ice'
      if (dist(width*0.833f, height*0.875f, mouseX, mouseY) < height*0.047f)//Changed to percent
      {
        lazarElementFire = false;//Decides the laser type; Fire or Ice
        fireScreen.fireShipGun();
      }
    }
    else if (appScreen == 3)//game over screen
    {
      //Will send the user back to the main menu
      if ((mouseX > width*0.222f) && (mouseY > height*0.687f) && (mouseX < width*0.777f) && (mouseY < height*0.766f) )//Changed to percent
      {
        appScreen = 0;
      }
      //Will send the user back to the main menu
      if ((mouseX > width*0.222f) && (mouseY > height*0.422f) && (mouseX < width*0.777f) && (mouseY < height*0.656f) )//Changed to percent
      {
        appScreen = 4;
      }
    }
    else if (appScreen == 4)//Highscore Screen
    {
      //Will send the user back to the main menu
      if ((mouseX > width*0.222f) && (mouseY > height*0.813f) && (mouseX < width*0.777f) && (mouseY < height*0.89f) )//Changed to percent
      {
        appScreen = 0;
      }
      //back to main menu button
    }
  }
  else
  {
    //Keyboard controls for computer, only for moving the ship and shooting, unable to navigate menus.

    if (key == CODED && appScreen == 1)
    {
      if (keyCode == LEFT)
      {
        classicScreen.moveShipLeft();
      }
      if (keyCode == RIGHT)
      {
        classicScreen.moveShipRight();
      }
      if (keyCode == UP)
      {
        classicScreen.fireShipGun();
      }
    }
    else if (key == CODED && appScreen == 2)
    {
      if (keyCode == LEFT)
      {
        fireScreen.moveShipLeft();
      }
      if (keyCode == RIGHT)
      {
        fireScreen.moveShipRight();
      }
      if (keyCode == UP)
      {
        lazarElementFire = true;
        //println("up pressed");
        fireScreen.fireShipGun();
      }
      if (keyCode == DOWN)
      {
        lazarElementFire = false;
        fireScreen.fireShipGun();
      }
    }
  }
}




///////////////////////////Screen Display///////////////////////////
///////////////////////////Screen Display///////////////////////////
///////////////////////////Screen Display///////////////////////////
//Displays the screen based on the appScreen number
//screen Numbers, 0 main Menu, 1 Classic Game, 2 Fire and Ice Game, 3 Game over, 4 Highscores
public void screenDisplay()
{
  //Displays the main menu as well as resetting values used in other games
  if (appScreen == 0)//Starting/main menu
  {
    
    mainMenuGUI();
    if(requestInfo == true)
    {
      imageMode(CORNER);
      image(trainUI,0,0,width,height);
      imageMode(CENTER);
    }
    playerLives = 3;
    playerScore = 0;
    firePlayerScore = 0;
    ringLevel = 0;
  }

  //Displays the ClassicMode draw to screen function. and tells the APP which gamemode was 
  //played last. It does this so the highscores can tell which score came from what game
  else if (appScreen == 1)//classic game mode screen
  {
    fireMode = false;//says which gamemode was played. 
    lazarState = classicScreen.lazarOn; // changes the lazarState for more details see ClassicMode
    classicScreen.drawToScreen();//Draws the visual items from ClassicMode, more details in ClassicMode
    loopShapeCreation(1);//tells the LoopShapeCreation Function which gamemode is playing 1 for classic, 2 for fire.
  }
  else if (appScreen == 2)//Fire and Ice GameMode Screen
  {
    //copy of above section just working for the FireAndIceMode Class not ClassicMode
    fireMode = true;
    lazarState = fireScreen.lazarOn;
    fireScreen.drawToScreen();
    loopShapeCreation(2);
  }
  else if (appScreen == 3)//game over screen
  {
    gameOverGUI();
  }
  else if (appScreen == 4)
  {
    //Loads the high score GUI
    highscoreGUI();
    //Checks if highscores have been updated, if not it will load scores from previous sessions from text file.
    if (updatedHighscores == false)
    {
      loadHighscores();
    }
    else if (updatedHighscores == true)
    {
      saveHighscores();
    }
  }
}

//Function that talks to the gamemode subclasses and tells it when to start going through the second set of rings
//as well as telling it what level it should be (level affects speed and scale rate of the shapes.)
//modeSelected is used to tell which mode the user is playing.
//
//Basically how it works is it checks when the last object of the pattern1 array inside the subclass has been destoryed or hit
//then it moves to the pattern2 array and once it hits the end of that it re populates each array with new values based on the
//ringlevel value.
public void loopShapeCreation(int modeSelected)
{
  if (modeSelected == 1)
  {
    if (classicScreen.pattern2.get(classicScreen.patternSize -1).shapeState == 4)
    {
      if (levelUp == false)
      {
        levelUp = true;
        ringLevel += 1;
        classicScreen.arrayChoice = 1; 
        classicScreen.createRings(ringLevel);
      }
    }
    else if (classicScreen.pattern1.get(classicScreen.patternSize - 1).shapeState == 4)
    {

      classicScreen.arrayChoice = 2; 
      println("choice " + classicScreen.arrayChoice);
      levelUp = false;
    }
  }

  else if (modeSelected == 2)
  {
    if (fireScreen.firePattern2.get(fireScreen.firePatternSize -1).shapeState == 4)
    {
      if (levelUp == false)
      {
        levelUp = true;
        ringLevel += 1;
        fireScreen.arrayChoice = 1; 
        fireScreen.createRings(ringLevel);
      }
    }
    else if (fireScreen.firePattern1.get(fireScreen.firePatternSize -1).shapeState == 4)
    {
      fireScreen.arrayChoice = 2; 
      levelUp = false;
    }
  }
}



/// scores/// 

//Function set up to store the scores from users playing the classic game mode.
public void saveHighscores()
{
  //Creating a string array from the int array of scores and then using the saveStrings function to save to chosen text doc
  String[] scoreString = str(scoreListNew);
  saveStrings(dataPath("Highscores.txt"), scoreString);
  //Loop to display the top 5 scores from the text document.
  for (int i = 0; i < 5; i++)
  {
    text(scoreListNew[i], width*0.222f, height*0.234f+(i*(height*0.078f)));//Changed to percent
  }
  //Function set up to store the scores from users playing the Fire and Ice game mode.
  //Creating string array from the int array of scores and then using the saveStrings function to save to chosen text doc
  String[] fireScoreString = str(fireScoreListNew);
  saveStrings(dataPath("FireHighscores.txt"), fireScoreString);
  //Loop to display the top 5 scores from the fire and ice text doc
  for (int i = 0; i < 5; i++)
  {
    text(fireScoreListNew[i], width*0.777f, height*0.234f+(i*(height*0.078f)));//Changed to percent
  }
}
//Function set up to load the scores from the text document to populate the scores arrays. This allows the user to access the information on the highscores screen if desired
public void loadHighscores()
{
  //Using loadStrings to populate a created string array with the information from the text doc
  String[] tempScores = loadStrings("Highscores.txt");
  //Converting the string array into a new int array so that the values can be sorted and displayed as desired
  scoreListOld = PApplet.parseInt(tempScores);
  //Loop to display the top 5 scores
  for (int i = 0; i < 5; i++)
  {
    text(scoreListOld[i], width*0.222f, height*0.234f+(i*(height*0.078f)));//Changed to percent
  }
  //Repeating the process for the fire and ice game mode which has its own tet document and set of high scores
  String[] fireTempScores = loadStrings("FireHighscores.txt");
  fireScoreListOld = PApplet.parseInt(fireTempScores);
  //Loop to display the top 5 scores.
  for (int i = 0; i < 5; i++)
  {
    text(fireScoreListOld[i], width*0.777f, height*0.234f+(i*(height*0.078f)));//Changed to percent
  }
}

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
  public void drawToScreen()
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

  public void pickLazarColour()
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


  public void controlGui()
  {
    //bottom control area
    rect(0, height*0.766f, width, height*0.234f);//Changed to percent
    //button area
    rect(width*0.222f, height*0.797f, width*0.555f, height*0.156f);//Changed to percent
    //fire button
    ellipse(width/2, height*0.875f, height*0.094f, height*0.094f);//Changed to percent
    //left button
    rect(width*0.222f, height*0.797f, width*0.18f, height*0.156f);//Changed to percent
    //right button 
    rect(width*0.597f, height*0.797f, width*0.18f, height*0.156f);//Changed to percent /
    imageMode(CORNER);
    image(classicUI,0,0,width,height);
    imageMode(CENTER);
  }

  public void createRings(int level)
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
        pattern1.add(new RingCreation(i*2000, false, floor(random(6, 11)), 2+level1, height*0.00078f+(level1*(height*0.00078f)) ));//Changed to percent
      }
      else if (ran == 1)
      {
        pattern1.add(new RingCreation(i*2000, true, floor(random(6, 11)), 2+level1, height*0.00078f+(level1*(height*0.00078f)) ) );//Changed to percent
      }
    }

    for (int i = 0; i < patternSize; i++)
    {  
      int ran = floor(random(2));
      if (ran == 0)
      {
        pattern2.add(new RingCreation(i*2000, false, floor(random(6, 11)), 2+level2, height*0.00078f+(level2*(height*0.00078f)) ) );//Changed to percent
      }
      else if (ran == 1)
      {
        pattern2.add(new RingCreation(i*2000, true, floor(random(6, 11)), 2+level2, height*0.00078f+(level2*(height*0.00078f)) ) );//Changed to percent
      }
    }
  }

  public void drawShapes(ArrayList<RingCreation> array)
  {
    int arrayOfRadius[] = new int[array.size()];

    for (int i = 0; i < array.size(); i++)
    {
      RingCreation shape = array.get(i);

      if (shape.radius == 0 || shape.shapeDestroyed == true)
      {
        arrayOfRadius[i] = round(height*0.781f);//Changed to percent
        continue;
      }
      arrayOfRadius[i] = shape.radius;
      shape.drawShape();
    } 
    distFromRing = min(arrayOfRadius) - height*0.125f;//Changed to percent



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


  public void stopLazar(ArrayList<RingCreation> array)
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

  public void fireShipGun()
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
  public void lazar()
  {
    if (lazarOn == true || lazarVisable == true)
    {
      pushMatrix();
      translate(width/2, height*0.383f);//Changed to percent
      rotate(radians(shipAngleX));
      fill(lazarColour);
      rect(lazarX-(width*0.008f), lazarY-(height*0.005f), distFromRing, height*0.008f);
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
  public void drawToScreen()
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

  public void pickLazarColour()
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


  public void controlGui()
  {
    //bottom control area
    rect(0, height*0.766f, width, height*0.234f);//Changed to percent
    //button area
    rect(width*0.292f, height*0.797f, width*0.416f, height*0.156f);//Changed to percent
    //Ice Laser
    ellipse(width*0.166f, height*0.875f, width*0.166f, height*0.094f);//Changed to percent
    //Fire Laser
    ellipse(width*0.833f, height*0.875f, width*0.166f, height*0.094f);//Changed to percent
    //left button
    rect(width*0.292f, height*0.797f, width*0.208f, height*0.156f);//Changed to percent
    //right button 
    rect(width/2, height*0.797f, width*0.208f, height*0.156f);//Changed to percent
    imageMode(CORNER);
    image(fireUI,0,0,width,height);
    imageMode(CENTER);
  }

  public void createRings(int level)
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
        firePattern1.add(new FireAndIceRings(i*2000, false, floor(random(6,11)), 2+level1, height*0.00078f+(level1*(height*0.00078f)), true ));//Changed to percent
      }
      else if (ran == 1)
      {
        firePattern1.add(new FireAndIceRings(i*2000, true, floor(random(6,11)), 2+level1, height*0.00078f+(level1*(height*0.00078f)), false ) );//Changed to percent
      }
    }

    for (int i = 0; i < firePatternSize; i++)
    {  
      int ran = floor(random(2));
      if (ran == 0)
      {
        firePattern2.add(new FireAndIceRings(i*2000, false, floor(random(6,11)), 2+level2, height*0.00078f+(level2*(height*0.00078f)), true ) );//Changed to percent
      }
      else if (ran == 1)
      {
        firePattern2.add(new FireAndIceRings(i*2000, true, floor(random(6,11)), 2+level2, height*0.00078f+(level2*(height*0.00078f)), false ) );//Changed to percent
      }
    }
  }

  public void drawShapes(ArrayList<FireAndIceRings> array)
  {
    int arrayOfRadius[] = new int[array.size()];

    for (int i = 0; i < array.size(); i++)
    {
      FireAndIceRings shape = array.get(i);

      if (shape.radius == 0 || shape.shapeDestroyed == true)
      {
        arrayOfRadius[i] = round(height*0.781f);//Changed to percent
        continue;
      }
      arrayOfRadius[i] = shape.radius;
      shape.drawShape();
    } 
    distFromRing = min(arrayOfRadius) - height*0.125f;//Changed to percent



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


  public void stopLazar(ArrayList<FireAndIceRings> array)
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

  public void fireShipGun()
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
  public void lazar()
  {
    if (lazarOn == true || lazarVisable == true)
    {
      pushMatrix();
      translate(width/2, height*0.383f);//Changed to percent
      rotate(radians(shipAngleX));
      fill(lazarColour);
      rect(lazarX-(width*0.008f),lazarY-(height*0.005f),distFromRing, height*0.008f);
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
      shipReady = loadImage("Ship-Ready.png");
    }
    fill(255);
  }
}

//This class displays the ship and calculates all of its movement.
//However this class has very little use unless it is extended.
//This class is extended by the subclasses FireAndIceMode and ClassicMode
//Each of these extended classes are vital if this class is to be of any use.
class GameMethods
{
  //ship Variables
  float shipX, shipY, shipSpeed;
  int shipAngleY, shipAngleX;
  int shipColour;
  //laser related
  float lazarX, lazarY;
  boolean lazarOn, lazarHasBeenShot, lazarVisable;
  int timeFired;
  int lazarColour;

  //shape related
  float distFromRing;

  //Basic constructor that sets up default values for ease of use in the classes methods.
  GameMethods()
  {
    //ship related
    shipColour = color(43, 207, 211);
    shipSpeed = 5;
    shipX = cos(radians(shipAngleX))*(height*0.094f);//Changed to percent
    shipY = sin(radians(shipAngleY))*(height*0.094f);//Changed to percent
    shipAngleY = 270;
    shipAngleX = 270;
    //laser related
    lazarX = cos(radians(0))*(height*0.119f);//Changed to percent
    lazarY = sin(radians(0))*(height*0.119f);//Changed to percent

    lazarColour = color(170, 6, 6);

    lazarOn = false;
  }
  
  //Function that holds everything required for the draw loop.
  public void screen()
  {
    textSize(floor(height*0.05f));
    textAlign(LEFT);
    text(playerScore, width*0.694f, height*0.063f);//Changed to percent
    text(playerLives, width*0.073f, height*0.063f);//Changed to percent
    fill(255);
    
    //Loads GUI containing ship and planet
    gui();
    shipAngle = shipAngleX;
    if (playerLives == 0)
    {
      //lets the app know that highscores has been updated.
      //these two do the same thing just one is for FireAndIceMode the other for ClassicMode.
      //the boolean is turned on or off by the method that draws that game mode to the screen.
      updatedHighscores = true;
      if (fireMode == true)
      {
        playerLives = 3;
        appScreen = 3;
        playerScore = 0;
        //Taking the int array and populating it with the original array of scores and 
        //adding the current players score to it using the append method
        fireScoreListNew = append(fireScoreListOld, firePlayerScore);
        //We then sort this new array in order to have the scores be stored in a meaningful manner,
        // in this case the first number in the array will be the smallest
        fireScoreListNew = sort(fireScoreListNew);
        //We then use the reverse method on the same array in order to have the scores stored 
        //in decreasing number so that the highest score will be in [0] and then next highest in [1] etc.
        fireScoreListNew = reverse(fireScoreListNew);
        
        //We repeat the exact same process in the following three lines 
        //for the other game mode and its corresponding arrays
        scoreListNew = append(scoreListOld, playerScore);
        scoreListNew = sort(scoreListNew);
        scoreListNew = reverse(scoreListNew);
      }
      else if (fireMode == false)
      {
        playerLives = 3;
        firePlayerScore = 0;
        appScreen = 3;
        fireScoreListNew = append(fireScoreListOld, firePlayerScore);
        fireScoreListNew = sort(fireScoreListNew);
        fireScoreListNew = reverse(fireScoreListNew);
        scoreListNew = append(scoreListOld, playerScore);
        scoreListNew = sort(scoreListNew);
        scoreListNew = reverse(scoreListNew);
      }
    }
  }



  ///////////////////////////GUI///////////////////////////
  ///////////////////////////GUI///////////////////////////
  ///////////////////////////GUI///////////////////////////
  //Displays the planet and the ship
  public void gui()
  {
    gameAreaGUI();
  }

  //GUI that displays the planet and ship
  public void gameAreaGUI()
  {
    //draws the ship and corrects its rotation so it points away from planet at all times
    shipRotationCorrection();

    //planet
    ellipse(width/2, height*0.383f, width*0.181f, height*0.102f);//Changed to percent
    imageMode(CENTER);
    image(planetImg,width/2,height*0.383f, width*0.181f, height*0.102f);
  }


  ///////////////////////////Usefull Methods///////////////////////////
  ///////////////////////////Usefull Methods///////////////////////////
  ///////////////////////////Usefull Methods///////////////////////////
  
  //Method that stops angles going past 360 degrees.
  public int dontPass360(int degree)
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


  ///////////////////////////Ship Movement///////////////////////////
  ///////////////////////////Ship Movement///////////////////////////
  ///////////////////////////Ship Movement///////////////////////////

  public void shipRotationCorrection()
  {
    //Ship
    //Ship Movement explained.//
    //The ship is drawn from the center point so it orientates the right way.
    //The X and Y pos is rotatePoint + Cos(Angle)*DistanceFromX
    //because of translate, the rotation point isn't required as it is set by that.
    //Ship Rotation Explained//
    //To have the ship face the right way at all times we added a rotate function.
    //The rotate function uses the same angle as the ship does to stay in sync.
    //The rotate function rotates the grid, to compenstate for this the translate
    //function was used to place the rotation of the grid in the right place. 
    //The push and pop Matrix mark the start and ends of the rotation section.
    imageMode(CENTER);
    pushMatrix();
    translate(width/2, height*0.383f);//Changed to percent
    rotate(radians(shipAngleX));
    fill(shipColour);
    //rect(shipX, shipY, width*0.069, height*0.039);//Changed to percent
    image(shipReady,shipX, shipY, width*0.139f, height*0.078f);
    rectMode(CORNER);
    popMatrix();
    fill(255);
  }

  //moves the ship right
  public void moveShipRight()
  {
    shipAngleY += shipSpeed;
    shipAngleX += shipSpeed;
    resetShipAngle();
  }

  //moves the ship left
  public void moveShipLeft()
  {
    shipAngleY -= shipSpeed;
    shipAngleX -= shipSpeed;
    resetShipAngle();
  }

  //if the ship goes past 360 degress or below 0 degress it gets corrected with this function
  public void resetShipAngle()
  {

    shipAngleX = dontPass360(shipAngleX);
    shipAngleY = dontPass360(shipAngleY);
  }
}

///////////////////////////Classes///////////////////////////
///////////////////////////Classes///////////////////////////
///////////////////////////Classes///////////////////////////

class RingCreation
{
  float rScaleRate, rotateSpeed, rotateSpeedN;
  int weakPointColour;
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
  public void drawShape() 
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
      if (radius < height*0.133f)//Changed to percent
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
  public boolean delay()
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

  public int rotationDirection(int input)
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
  public int dont360(int degree)
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
  public boolean shapeActive()
  {
    if (shapeOn == true && delay() == true)
    {
      return true;
    }
    return false;
  }


  //Draws the shape with the amount of sides specified by the contructor.
  public void drawHex()
  {
    //starts pushMatrix so shape can rotate   
    pushMatrix();
    //Places in centre of game area
    translate(width/2, height*0.383f);//Changed to percent
    //rotates the shape depending on the degrees of the rotation variable
    rotate(radians(rotation));
    fill(0, 0);
    beginShape();
    //Loop that draws a hexagon, it also finds the points for the selected weak side.
    for (int i = 0; i <= shapeSides; i++) 
    {
      strokeWeight(floor(height*0.0047f));
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
    strokeWeight(floor(height*0.0016f));
    popMatrix();
  }

  public boolean lazarHitDetect()
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
  public void angleSelection()
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
  public void pickColour()
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
  public void drawShape() 
  { 
    pickColour();
    //Calls the ringcreation drawShapes method using super. to prevent conflicts
    super.drawShape();
  }

  //LazarHitDetect overrides the LazarHitDetect of the SuperClass to prevent complication. 
  //By doing this it allows the shape to detect what type of laser is hitting it. 
  public boolean lazarHitDetect()
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


}
