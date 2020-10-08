package blockbattle

class Worm {
    import introprog.PixelWindow
    import java.awt.{Color => JColor}

  def nextRandomPos(): Pos = {
    import scala.util.Random.nextInt
    val x = nextInt(Game.windowSize._1)
    val y = nextInt(Game.windowSize._2 - 9) + 9
    blockbattle.Pos(x,y)
  }

  var pos = nextRandomPos()
  def isHere(p: Pos): Boolean = pos == p
  val teleportProbability = 0.02
  def randomTeleport(notHere: Pos): Unit =  do pos = nextRandomPos() while (pos == notHere)

  def shouldTeleport: Boolean = (math.random() < teleportProbability)

}
