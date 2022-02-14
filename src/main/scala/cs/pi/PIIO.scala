package cs.pi

import chisel3._
import chisel3.util._

object PIIO {
  val CMD_IDLE: UInt = 0.U(2.W)
  val CMD_WR: UInt = 1.U(2.W)
  val CMD_RD: UInt = 2.U(2.W)

  val RSP_IDLE: UInt = 0.U(2.W)
  val RSP_DVA: UInt = 1.U(2.W)
  val RSP_FAIL: UInt = 2.U(2.W)
  val RSP_ERR: UInt = 3.U(2.W)
}

class PIIO (dataBW: Int = 16, addrBW: Int = 10) extends Bundle {
  // PI IO - master side
  val cmd: UInt = Input(UInt(2.W))
  val addr: UInt = Input(UInt(addrBW.W))
  val wdata: UInt = Input(UInt(dataBW.W))
  val rsp: UInt = Output(UInt(2.W))
  val rdata: UInt = Output(UInt(dataBW.W))

  def connectWrite(pi: PIIO) : Unit = {
    pi.cmd := cmd
    pi.addr := addr
    pi.wdata := wdata
  }

  def connectWrite(pis: Seq[PIIO]): Unit = {
    pis.map(pi => connectWrite(pi))
  }

  def connectRead(pis: Seq[PIIO]): Unit = {
    rdata := pis.map(a => a.rdata).reduce((a, b) => a | b)
    rsp := pis.map(a => a.rsp).reduce((a, b) => a | b)
  }
}

