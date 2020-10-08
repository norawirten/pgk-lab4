package blockbattle

class Mole(
  val name: String,
  var pos: Pos,
  var dir: (Int, Int),
  val color: java.awt.Color,
  val keyControl: KeyControl
){
  var points = 0

  override def toString =
    s"Mole[name=$name, pos=$pos, dir=$dir, points=$points]"

  /** Om keyControl.has(key) så uppdateras riktningen dir enligt keyControl */
  def setDir(key: String): Unit = (
    if (keyControl.has(key)) {
      dir = keyControl.direction(key)

    }
  )

  /** Uppdaterar dir till motsatta riktningen. */
  def reverseDir(): Unit = {
    dir = (dir._1 * -1, dir._2 * -1)

  }

  /** Uppdaterar pos så att den blir nextPos */
  def move(): Unit = (
    pos = nextPos
  )

  /** Ger nästa position enligt riktningen dir utan att uppdatera pos */
  def nextPos: Pos = (
    pos.moved (dir)
  )
}

