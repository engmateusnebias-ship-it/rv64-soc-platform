import chisel3._
import chisel3.util._

class Counter(width: Int) extends Module {
  val io = IO(new Bundle {
    val enable = Input(Bool())
    val count  = Output(UInt(width.W))
  })

  val reg = RegInit(0.U(width.W))

  when(io.enable) {
    reg := reg + 1.U
  }

  io.count := reg
}

object CounterGen extends App {
  _root_.circt.stage.ChiselStage.emitSystemVerilogFile(
    new Counter(8),
    args = Array("--target-dir", "generated"),
    firtoolOpts = Array("--strip-debug-info")
  )
}
