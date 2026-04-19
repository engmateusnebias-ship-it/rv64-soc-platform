import chisel3._

class Register(width: Int) extends Module {
  val io = IO(new Bundle {
    val d  = Input(UInt(width.W))
    val en = Input(Bool())
    val q  = Output(UInt(width.W))
  })

  val reg = RegInit(0.U(width.W))

  when(io.en) {
    reg := io.d
  }

  io.q := reg
}
