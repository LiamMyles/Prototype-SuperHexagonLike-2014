//This class displays the ship and calculates all of its movement.
//However this class has very little use unless it is extended.
//This class is extended by the subclasses FireAndIceMode and ClassicMode
//Each of these extended classes are vital if this class is to be of any use.
class GameMethods
{
  //ship Variables
  float shipX, shipY, shipSpeed;
  int shipAngleY, shipAngleX;
  color shipColour;
  //laser related
  float lazarX, lazarY;
  boolean lazarOn, lazarHasBeenShot, lazarVisable;
  int timeFired;
  color lazarColour;

  //shape related
  float distFromRing;

  //Basic constructor that sets up default values for ease of use in the classes methods.
  GameMethods()
  {
    //ship related
    shipColour = color(43, 207, 211);
    shipSpeed = 5;
    shipX = cos(radians(shipAngleX))*(height*0.094);//Changed to percent
    shipY = sin(radians(shipAngleY))*(height*0.094);//Changed to percent
    shipAngleY = 270;
    shipAngleX = 270;
    //laser related
    lazarX = cos(radians(0))*(height*0.119);//Changed to percent
    lazarY = sin(radians(0))*(height*0.119);//Changed to percent

    lazarColour = color(170, 6, 6);

    lazarOn = false;
  }
  
  //Function that holds everything required for the draw loop.
  void screen()
  {
    textSize(32);
    textAlign(LEFT);
    text(playerScore, width*0.694, height*0.063);//Changed to percent
    text(playerLives, width*0.073, height*0.063);//Changed to percent
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
  void gui()
  {
    gameAreaGUI();
  }

  //GUI that displays the planet and ship
  void gameAreaGUI()
  {
    //draws the ship and corrects its rotation so it points away from planet at all times
    shipRotationCorrection();

    //planet
    shapeMode(CENTER);
    ellipse(width/2, height*0.383, width*0.181, height*0.102);//Changed to percent
    shape(planetImg,width/2,height*0.383, width*0.181, height*0.102);
  }


  ///////////////////////////Usefull Methods///////////////////////////
  ///////////////////////////Usefull Methods///////////////////////////
  ///////////////////////////Usefull Methods///////////////////////////
  
  //Method that stops angles going past 360 degrees.
  int dontPass360(int degree)
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

  void shipRotationCorrection()
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
    shapeMode(CENTER);
    pushMatrix();
    translate(width/2, height*0.383);//Changed to percent
    rotate(radians(shipAngleX));
    fill(shipColour);
    //rect(shipX, shipY, width*0.069, height*0.039);//Changed to percent
    shape(shipReady,shipX, shipY, width*0.139, height*0.078);
    rectMode(CORNER);
    popMatrix();
    fill(255);
  }

  //moves the ship right
  void moveShipRight()
  {
    shipAngleY += shipSpeed;
    shipAngleX += shipSpeed;
    resetShipAngle();
  }

  //moves the ship left
  void moveShipLeft()
  {
    shipAngleY -= shipSpeed;
    shipAngleX -= shipSpeed;
    resetShipAngle();
  }

  //if the ship goes past 360 degress or below 0 degress it gets corrected with this function
  void resetShipAngle()
  {

    shipAngleX = dontPass360(shipAngleX);
    shipAngleY = dontPass360(shipAngleY);
  }
}

