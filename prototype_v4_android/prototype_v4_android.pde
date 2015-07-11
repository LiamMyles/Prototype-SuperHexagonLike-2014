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


void setup()
{
  //basic setup loop for background, screen size, text size and framerate.
  background(255);
  //size(360, 640);
  frameRate(30);
  smooth();
  textSize(floor(height*0.05));

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


void draw()
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
void mainMenuGUI()
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
void highscoreGUI()
{
  rect(width*0.222, height*0.81, width*0.555, height*0.078);//Changed to percent
  imageMode(CORNER);
  image(highscoreUI,0,0,width*0.99999,height*0.99999);
  imageMode(CENTER);
}

//Draws the game over GUI
void gameOverGUI()
{
  //Displays the game over screen
  textSize(floor(height*0.0781));
  textAlign(CENTER);
  text("Game Over", width/2, height*0.383);//Changed to percent
  textSize(floor(height*0.0469));
  if (fireMode == true)
  {
    text("You scored "+firePlayerScore, width/2, height*0.461);//Changed to percent
  }
  else if (fireMode == false)
  {
    text("You scored "+playerScore, width/2, height*0.461);
  }
  
  rect(width*0.222, height*0.687, width*0.555, height*0.078);//Changed to percent
  rect(width*0.222, height*0.578, width*0.555, height*0.078);//Changed to percent
  imageMode(CORNER);
  image(gameoverUI,0,0,width,height);
  imageMode(CENTER);
}
////////////////////////////////////CONTROLS////////////////////////////////////
////////////////////////////////////CONTROLS////////////////////////////////////
////////////////////////////////////CONTROLS////////////////////////////////////

//Keyboard controls used for testing.
void keyReleased()
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
void mousePressed()
{
  mouseDown = true;
  //println(mouseDown);
}

void mouseReleased()
{
  mouseDown = false;
  requestInfo = false;
  //println(mouseDown);
}


//Function thats runs when mouse is held down, otherwise it does nothing.
//This is used for the 'hit' areas of all the UI buttons
void mouseControls()
{
  if (mouseDown == true)
  {
    if (appScreen == 0)//Starting/main menu
    {
      //Launches classic game mode and creates the rings inside the ClassicMode subclass
      if ( (mouseX > width*0.222) && (mouseY > height*0.234) && (mouseX < width*0.777) && (mouseY < height*0.31))//Changed to percent
      {
        appScreen = 1;
        classicScreen.createRings(ringLevel);
      }
      //Launches fire and ice game mode and creates the rings inside the FireAndIceMode class
      if ( (mouseX > width*0.222) && (mouseY > height*0.344) && (mouseX < width*0.777) && (mouseY < height*0.422))//Changed to percent
      {
        appScreen = 2;
        fireScreen.createRings(ringLevel);
      }
      //Launches the high scores page
      if ( (mouseX > width*0.222) && (mouseY > height*0.453) && (mouseX < width*0.777) && (mouseY < height*0.53))//Changed to percent
      {
        appScreen = 4;
      }
      if (dist(width*0.903, height*0.055, mouseX, mouseY) < height*0.055/2)//Changed to percent
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
      if ( (mouseX > width*0.222) && (mouseY > height*0.797) && (mouseX < width*0.403) && (mouseY < height*0.953) )//Changed to percent
      {
        classicScreen.moveShipLeft();
      }
      //Moves the ClassicMode ship right
      if ((mouseX > width*0.597) && (mouseY > height*0.797) && (mouseX < width*0.777) && (mouseY < height*0.953) )//Changed to percent
      {
        classicScreen.moveShipRight();
      }
      //Will shoot the ClassicMode ship gun/laser
      if (dist(width*0.50, height*0.875, mouseX, mouseY) < height*0.047)//Changed to percent
      {
        classicScreen.fireShipGun();
      }
    }
    else if (appScreen == 2)//Fire and Ice GameMode Screen
    {
      //Moves the FireAndIceMode ship left
      if ( (mouseX > width*0.292) && (mouseY > height*0.797) && (mouseX < width*0.50) && (mouseY < height*0.953) )//Changed to percent
      {
        fireScreen.moveShipLeft();
      }
      //Moves the FireAndIceMode ship right
      if ((mouseX > width*0.50) && (mouseY > height*0.797) && (mouseX < width*0.708) && (mouseY < height*0.953) )//Changed to percent
      {
        fireScreen.moveShipRight();
      }
      //Fires the FireAndIceMode ship gun and tells the app the the laser is 'fire'
      if (dist(width*0.166, height*0.875, mouseX, mouseY) < height*0.047)//Changed to percent
      {
        lazarElementFire = true;//Decides the laser type; Fire or Ice
        fireScreen.fireShipGun();
      }
      //Fires the FireAndIceMode ship gun and tells the app the the laser is 'ice'
      if (dist(width*0.833, height*0.875, mouseX, mouseY) < height*0.047)//Changed to percent
      {
        lazarElementFire = false;//Decides the laser type; Fire or Ice
        fireScreen.fireShipGun();
      }
    }
    else if (appScreen == 3)//game over screen
    {
      //Will send the user back to the main menu
      if ((mouseX > width*0.222) && (mouseY > height*0.687) && (mouseX < width*0.777) && (mouseY < height*0.766) )//Changed to percent
      {
        appScreen = 0;
      }
      //Will send the user back to the main menu
      if ((mouseX > width*0.222) && (mouseY > height*0.422) && (mouseX < width*0.777) && (mouseY < height*0.656) )//Changed to percent
      {
        appScreen = 4;
      }
    }
    else if (appScreen == 4)//Highscore Screen
    {
      //Will send the user back to the main menu
      if ((mouseX > width*0.222) && (mouseY > height*0.813) && (mouseX < width*0.777) && (mouseY < height*0.89) )//Changed to percent
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
void screenDisplay()
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
void loopShapeCreation(int modeSelected)
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
void saveHighscores()
{
  //Creating a string array from the int array of scores and then using the saveStrings function to save to chosen text doc
  String[] scoreString = str(scoreListNew);
  saveStrings(dataPath("Highscores.txt"), scoreString);
  //Loop to display the top 5 scores from the text document.
  for (int i = 0; i < 5; i++)
  {
    text(scoreListNew[i], width*0.222, height*0.234+(i*(height*0.078)));//Changed to percent
  }
  //Function set up to store the scores from users playing the Fire and Ice game mode.
  //Creating string array from the int array of scores and then using the saveStrings function to save to chosen text doc
  String[] fireScoreString = str(fireScoreListNew);
  saveStrings(dataPath("FireHighscores.txt"), fireScoreString);
  //Loop to display the top 5 scores from the fire and ice text doc
  for (int i = 0; i < 5; i++)
  {
    text(fireScoreListNew[i], width*0.777, height*0.234+(i*(height*0.078)));//Changed to percent
  }
}
//Function set up to load the scores from the text document to populate the scores arrays. This allows the user to access the information on the highscores screen if desired
void loadHighscores()
{
  //Using loadStrings to populate a created string array with the information from the text doc
  String[] tempScores = loadStrings("Highscores.txt");
  //Converting the string array into a new int array so that the values can be sorted and displayed as desired
  scoreListOld = int(tempScores);
  //Loop to display the top 5 scores
  for (int i = 0; i < 5; i++)
  {
    text(scoreListOld[i], width*0.222, height*0.234+(i*(height*0.078)));//Changed to percent
  }
  //Repeating the process for the fire and ice game mode which has its own tet document and set of high scores
  String[] fireTempScores = loadStrings("FireHighscores.txt");
  fireScoreListOld = int(fireTempScores);
  //Loop to display the top 5 scores.
  for (int i = 0; i < 5; i++)
  {
    text(fireScoreListOld[i], width*0.777, height*0.234+(i*(height*0.078)));//Changed to percent
  }
}

