module DualPortRAM
#(parameter DATA_WIDTH = 16, ADDR_WIDTH = 10)
(
 input [(DATA_WIDTH-1):0]      data_a, data_b,
 input [(ADDR_WIDTH-1):0]      addr_a, addr_b,
 input                         we_a, we_b, clock,
 output reg [(DATA_WIDTH-1):0] q_a, q_b
);
   // Declare the RAM variable
   reg [DATA_WIDTH-1:0]        ram[2**ADDR_WIDTH-1:0];
   integer i;

   initial begin
    for (i = 0; i < 2**ADDR_WIDTH; i = i + 1) begin
      ram[i] = {DATA_WIDTH{1'b0}};
    end
   end
   
   always @ (posedge clock)  begin // Port a
      if (we_a) begin
	 ram[addr_a] <= data_a;
	 q_a <= data_a;
      end
      else 
	q_a <= ram[addr_a];
   end
   always @ (posedge clock) begin // Port b
      if (we_b) begin
	 ram[addr_b] <= data_b;
	 q_b <= data_b;
      end
      else
	q_b <= ram[addr_b];
   end
endmodule
