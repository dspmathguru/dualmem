package cs.pi

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class PIDualMemoryTest extends AnyFreeSpec with ChiselScalatestTester {
  val addrBW: Int = 10
  val depth: Int = 1 << addrBW
  val dataBW: Int = 16
  val dataDepth = 1 << addrBW

  val testLength = 1000

  val rand = scala.util.Random

  def testPIPort(port: PIIO, clock: Clock): Unit = {
    val testValues = for {i <- 1 to testLength} yield (i, rand.nextInt(dataDepth))
    val step = 1

    port.cmd.poke(PIIO.CMD_WR)
    for ((addr, data) <- testValues) {
      port.addr.poke(addr.U(addrBW.W))
      port.wdata.poke(data.U(dataBW.W))
      clock.step(step)
    }

    port.cmd.poke(PIIO.CMD_RD)
    for ((addr, data) <- testValues) {
      port.addr.poke(addr.U(addrBW.W))
      clock.step(1)
      port.rdata.expect(data.U)
    }
  }

  def testMemPort(port: MemIO, clock: Clock): Unit = {
    val testValues = for {i <- 1 to testLength} yield (i, rand.nextInt(dataDepth))
    val step = 1

    port.write.poke(true.B)
    port.enable.poke(true.B)
    for ((addr, data) <- testValues) {
      port.addr.poke(addr.U(addrBW.W))
      port.dataIn.poke(data.U(dataBW.W))
      clock.step(step)
    }
    port.write.poke(false.B)
    port.enable.poke(false.B)
    clock.step(step)

    port.enable.poke(true.B)
    for ((addr, data) <- testValues) {
      port.addr.poke(addr.U(addrBW.W))
      clock.step(1)
      port.dataOut.expect(data.U)
    }
    port.enable.poke(false.B)
    clock.step(1)
  }

  "Tests PI write and then read to PIDualMemory" in {
    test(new PIDualMemory(addrBW, depth, dataBW)).withAnnotations(Seq(VerilatorBackendAnnotation, WriteVcdAnnotation)) { dut =>
      fork { testPIPort(dut.piPort, dut.clock) }
      testMemPort(dut.memPort, dut.clock)
      dut.clock.step(5)
    }
  }
}
