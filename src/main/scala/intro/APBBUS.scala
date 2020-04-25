// See README.md for license details.

package intro

import chisel3._

class APBBUS(val addr_width: Int, val num_of_slave: Int, val data_width: Int) extends Module {
  require(util.isPow2(num_of_slave))
  val sel_width = util.log2Ceil(num_of_slave) 

  val io = IO(new Bundle{
    val MASTER = new APBIF(addr_width, data_width)
    val SLAVE  = Vec(num_of_slave, Flipped(new APBIF(addr_width - sel_width, data_width))) 
  })
  io.SLAVE.map(x => x <> io.MASTER)
  
  val sels = WireInit(io.MASTER.PADDR(addr_width - 1, addr_width - sel_width))
  val slave_sels = io.SLAVE.map(_.PSEL).zipWithIndex.foreach{
    case (x:Bool,i:Int) => x := Mux(sels === i.U, io.MASTER.PSEL, false.B) 
  }
 
  io.MASTER.PREADY  := io.SLAVE(sels).PREADY
  io.MASTER.PRDATA  := io.SLAVE(sels).PRDATA
  io.MASTER.PSLVERR := io.SLAVE(sels).PSLVERR
}

object APBBUS extends App {
  chisel3.Driver.execute(args, () => new APBBUS(8, 4, 32))
}

