import chisel3._

class Mux2(width: Int) extends Module {
  val io = IO(new Bundle {
    val sel = Input(Bool())
    val a   = Input(UInt(width.W))
    val b   = Input(UInt(width.W))
    val out = Output(UInt(width.W))
  })

  io.out := Mux(io.sel, io.a, io.b)
}
