# rv64-soc-platform

RV64GC SoC built with Rocket Chip and Chisel — targeting OpenSBI + Linux boot,
TileLink interconnect, and professional verification via UVM and Cocotb.

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                  RV64GC SoC                         │
│                                                     │
│  ┌──────────────┐   TileLink   ┌─────────────────┐  │
│  │  Rocket Core │◄────────────►│  L2 Cache /     │  │
│  │  (RV64GC)    │              │  Memory XBAR    │  │
│  │  M/S/U Mode  │              └────────┬────────┘  │
│  └──────────────┘                       │           │
│                              ┌──────────┼──────┐    │
│                              ▼          ▼      ▼    │
│                            DRAM       CLINT   PLIC  │
│                            CTRL       UART    GPIO  │
└─────────────────────────────────────────────────────┘
```

## Memory Map

| Base Address   | Size   | Device          |
|----------------|--------|-----------------|
| `0x0000_0000`  | 64 KiB | Boot ROM        |
| `0x0200_0000`  | 64 KiB | CLINT           |
| `0x0C00_0000`  | 4 MiB  | PLIC            |
| `0x1000_0000`  | 4 KiB  | UART 16550      |
| `0x8000_0000`  | 1 GiB  | DRAM            |

## Boot Sequence

```
ZSBL (Boot ROM)
  └─► OpenSBI (M-Mode firmware)
        └─► Linux kernel (S-Mode)
              └─► BusyBox init (U-Mode)
```

## Build

```bash
# Requirements: JDK 21, SBT 1.10+, firtool 1.144.0, Verilator 5.x
# RISC-V toolchains: riscv64-unknown-elf-gcc, riscv64-linux-gnu-gcc

# Generate RTL
sbt "runMain config.RV64SoCTop"

# Run Cocotb integration tests
make -C verif/cocotb

# Run UVM regression
make -C verif/uvm
```

## Status

| Phase | Description                        | Status      |
|-------|------------------------------------|-------------|
| 1     | Environment setup                  | ✅ Complete |
| 2     | Chisel RTL + SBT project           | 🔄 In progress |
| 3     | Verilator + Cocotb smoke tests     | ⬜ Planned  |
| 4     | Peripherals + Device Tree          | ⬜ Planned  |
| 5     | OpenSBI boot                       | ⬜ Planned  |
| 6     | Linux + BusyBox boot               | ⬜ Planned  |
| 7     | UVM TileLink verification          | ⬜ Planned  |
| 8     | riscv-formal ISA checking          | ⬜ Planned  |

## Evolution

This project builds on a handcrafted
[RV32I core implemented in VHDL](https://github.com/engmateusnebias-ship-it/rv32i-minimal-soc),
which established a solid understanding of pipeline stages, hazard handling,
and instruction decoding. This SoC takes that foundation to an industrial
level: parametric hardware generation with Rocket Chip and Chisel, a full
privilege stack (M/S/U-Mode) running OpenSBI and Linux, TileLink as the
system interconnect, and professional verification using UVM and Cocotb.

## License

MIT
