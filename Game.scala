package blockbattle

import java.awt.{Color => JColor}

object Game {
  val windowSize = (30, 50)
  val windowTitle = "EPIC BLOCK BATTLE"
  val blockSize = 14
  val skyRange    = 0 to 7
  val grassRange  = 8 to 8
 

  object Color { 
    val black = new JColor( 0, 0, 0)
    val mole1 = new JColor(51, 51, 51)
    val mole2 = new JColor(139,69,19)
    val soil = new JColor(153, 102, 51)
    val tunnel = new JColor(204, 153, 102)
    val grass = new JColor( 25, 130, 35)
    val sky = new JColor(135,206,250)
    val worm = new JColor(225, 100, 235)
    }
  /** Used with the different ranges and eraseBlocks */
  def backgroundColorAtDepth(y: Int): java.awt.Color = (
  if (skyRange.contains(y)){
    Color.sky
  }
  else if (grassRange.contains(y)){
    Color.grass
  }
  else {
    Color.soil
  }
  )
}


class Game(
  val leftPlayerName: String  = "LEFT",
  val rightPlayerName: String = "RIGHT",
  var leftPlayerPos: Pos,
  var rightPlayerPos: Pos,
  var leftPlayerDir: (Int, Int),
  var rightPlayerDir: (Int, Int),
  val leftPlayerColor: java.awt.Color,
  val rightPlayerColor: java.awt.Color, 
  var leftplayerKeyControl: KeyControl,
  var rightplayerKeyControl: KeyControl,
  var leftPlayerPoints: Int = 0,
  var rightPlayerPoints: Int = 0,

) {
 import Game._ // direkt tillgång till namn på medlemmar i kompanjon

 val window  = new BlockWindow(windowSize, windowTitle, blockSize)
 val leftMole: Mole  = new Mole(leftPlayerName, leftPlayerPos, leftPlayerDir, leftPlayerColor, leftplayerKeyControl)
 val rightMole: Mole = new Mole(rightPlayerName, rightPlayerPos, rightPlayerDir, rightPlayerColor, rightplayerKeyControl)
 val worms: Vector [Worm] = Vector (new Worm(), new Worm(), new Worm())

 def drawWorld(): Unit = {
   for(y <- 0 until windowSize._2){
    for(x <- 0 until windowSize._1){
      window.setBlock(Pos(x, y), backgroundColorAtDepth(y))
   }
  }


 }
 /** Use to erase old points, e.g updated score */
 def eraseBlocks(x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
   for(y <- y1 until y2){
    for(x <- x1 until x2){
      window.setBlock(Pos(x, y), backgroundColorAtDepth(y))
   }
  }
 }


 def update(mole: Mole): Unit = {
   
   
   if (mole.nextPos.y<=9|| mole.nextPos.y >= 50 || mole.nextPos.x <= 0 || mole.nextPos.x >= 30) {

     mole.reverseDir
     }

   //if (leftMole.pos == rightMole.pos) {quit = true}
    window.setBlock(mole.nextPos, mole.color)
   window.setBlock(mole.pos, Color.tunnel)

    mole.move()
       
 }  // update, draw new, erase old

 def update(worm: Worm): Unit = {
   if (worm.shouldTeleport){
     teleport(worm)
   }

   if (worm.isHere(leftMole.pos)){
     teleport(worm)
     leftPlayerPoints += 100
   }

   if (worm.isHere(rightMole.pos)){
     teleport(worm)
     rightPlayerPoints += 100
   }

   window.setBlock(worm.pos, Color.worm)

 }

 def teleport(worm: Worm): Unit = {
   window.setBlock(worm.pos, Color.soil)
   worm.randomTeleport (worm.pos)
 }

 def checkPoints = {
    if (leftPlayerPoints == 3000){
        quit = true
}; if (rightPlayerPoints == 3000){
    quit = true
}
}

var quit = false
val delayMillis = 80


 def gameLoop(): Unit = {
   while(!quit) {
     val t0 = System.currentTimeMillis
     handleEvents() //ändrar riktning vid tangettryck etc.
     update(leftMole) //flyttar, ritar, suddar etc.
     update(rightMole)
     for (w <- worms)  update(w)
     eraseBlocks(0, 0, 30, 2)   
     window.write("LEFT PLAYER POINTS: " + leftPlayerPoints,Pos(0,0), Color.black, 10)
     window.write("RIGHT PLAYER POINTS: " + rightPlayerPoints,Pos(19,0), Color.black, 10)

     if (window.getBlock(Pos(leftMole.pos.x + leftMole.dir._1, leftMole.pos.y + leftMole.dir._2)) == Color.soil) {leftPlayerPoints += 10}
     if (window.getBlock(Pos(rightMole.pos.x + rightMole.dir._1, rightMole.pos.y + rightMole.dir._2)) == Color.soil) {rightPlayerPoints += 10}
     checkPoints
     val elapsedMillis = (System.currentTimeMillis - t0).toInt
     Thread.sleep((delayMillis - elapsedMillis) max 0)
   }
 }

 def handleEvents(): Unit = {
  var e = window.nextEvent()
  while (e != BlockWindow.Event.Undefined) {
    e match{
      case BlockWindow.Event.KeyPressed(key) =>
      leftMole.setDir(key)
      rightMole.setDir(key) // ändra riktning på resp. mullvad
      case BlockWindow.Event.WindowClosed =>
      quit = true // avsluta spel-loopen
      }
      e = window.nextEvent()
    }
  }


 def start(): Unit = {
   println("Start digging!")
   println(s"$leftPlayerName ${leftMole.keyControl}")
   println(s"$rightPlayerName ${rightMole.keyControl}")
   drawWorld()
   gameLoop()
  
   if (quit == true) {window.write("GAME OVER",Pos(8,25), Color.black, 30)
   window.write("LEFT PLAYER POINTS: " + leftPlayerPoints, Pos(7, 28), Color.black, 15)
   window.write("RIGHT PLAYER POINTS: " + rightPlayerPoints, Pos(7, 30), Color.black, 15)
   }
 }

 def main(args: Array[String]): Unit = {
  start
  }
}




