package blockbattle

object Main {
  def main(args: Array[String]): Unit = {
    println("TODO: blockbattle")
    var p1Pos = Pos(12, 35)
    var p2Pos = Pos(15, 35)
    var p1Dir = (0, -1)
    var p2Dir = (0, -1)
    var p1Key = KeyControl("a", "d", "w", "s")
    var p2Key = KeyControl("Left", "Right", "Up", "Down")
    var game = new Game("LEFT", "RIGHT", p1Pos, p2Pos, p1Dir, p2Dir, Game.Color.mole1, Game.Color.mole2, p1Key, p2Key)

    game.start

  }
}
