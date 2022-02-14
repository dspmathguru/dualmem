package cs.pi

import chisel3._
import chisel3.util._

class DualPortRAM extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val data_a = Input(UInt(16.W))
    val data_b = Input(UInt(16.W))
    val addr_a = Input(UInt(10.W))
    val addr_b = Input(UInt(10.W))
    val we_a = Input(Bool())
    val we_b = Input(Bool())
    val clock = Input(Clock())
    val q_a = Output(UInt(16.W))
    val q_b = Output(UInt(16.W))
  })
  addResource("/DualPortRAM.v")
}

class PIDualMemory(dataBW: Int = 16, addrBW: Int = 10, depth: Int = 1024) extends Module {
  val piPort = IO(new PIIO(dataBW, addrBW))
  val memPort = IO(new MemIO(dataBW, addrBW))

  val ram = Module(new DualPortRAM)
  ram.io.clock := clock

  // PI port
  val rsp0 = Reg(UInt(2.W))
  when (piPort.cmd === PIIO.CMD_RD) { rsp0 := PIIO.RSP_DVA }
    .otherwise { rsp0 := PIIO.RSP_IDLE }

  piPort.rsp := rsp0
  ram.io.addr_a := piPort.addr
  ram.io.data_a := piPort.wdata
  piPort.rdata := ram.io.q_a
  ram.io.we_a := piPort.cmd === PIIO.CMD_WR

  // Memory Port
  ram.io.data_b := memPort.dataIn
  ram.io.addr_b := memPort.addr
  ram.io.we_b := memPort.write && memPort.enable
  memPort.dataOut := ram.io.q_b
}
