package cs.pi

import chisel3._

class MemIO(dataBW: Int = 16, addrBW: Int = 12) extends Bundle {
  val enable = Input(Bool())
  val write = Input(Bool())
  val addr = Input(UInt(addrBW.W))
  val dataIn = Input(UInt(dataBW.W))
  val dataOut = Output(UInt(dataBW.W))
}
