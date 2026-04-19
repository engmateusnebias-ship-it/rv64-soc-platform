# rv64-soc-platform

[![CI](https://github.com/engmateusnebias-ship-it/rv64-soc-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/engmateusnebias-ship-it/rv64-soc-platform/actions/workflows/ci.yml)

A parametric RV64GC SoC platform demonstrating end-to-end competency
across RTL generation, professional verification, and full software stack
integration.

## Architecture

| Component         | Details                              |
|-------------------|--------------------------------------|
| Core              | Rocket Core RV64GC, M/S/U modes      |
| L1 caches         | 32 KB I$ + 32 KB D$, 4-way           |
| Interconnect      | TileLink TL-UL / TL-UH / TL-C        |
| Topology          | Parametric crossbar (XBAR)           |
| Peripherals       | CLINT, PLIC, UART 16550, SPI, GPIO   |
| Memory            | 1 GB DRAM @ 0x8000_0000              |

## Stack

| Layer              | Technology                          |
|--------------------|-------------------------------------|
| RTL generator      | Rocket Chip (chipsalliance/master)  |
| HDL                | Chisel 6.7.0 + firtool 1.144.0      |
| Build tool         | Mill 0.11.7                         |
| Language           | Scala 2.13                          |
| JDK                | OpenJDK 21                          |
| Simulation         | Verilator 5.x                       |
| Integration tests  | Cocotb (Python)                     |
| Protocol verif.    | UVM (SystemVerilog IEEE 1800.2)     |
| ISA verification   | riscv-formal + SAIL golden model    |
| Firmware           | OpenSBI (M-Mode)                    |
| OS                 | Linux kernel (RISC-V)               |
| Userspace          | BusyBox                             |

## Memory map

| Base address   | Size   | Peripheral     |
|----------------|--------|----------------|
| `0x0000_0000`  | 64 KB  | Boot ROM       |
| `0x0200_0000`  | 64 KB  | CLINT          |
| `0x0C00_0000`  |  4 MB  | PLIC           |
| `0x1000_0000`  |  4 KB  | UART 16550     |
| `0x1000_1000`  |  4 KB  | SPI            |
| `0x1000_2000`  |  4 KB  | GPIO (16x)     |
| `0x8000_0000`  |  1 GB  | DRAM           |

## Boot sequence

1. **Boot ROM** (M-Mode) — minimal startup, jumps to OpenSBI
2. **OpenSBI** (M-Mode) — SBI calls, interrupt delegation, trap handler
3. **Linux kernel** (S-Mode) — MMU Sv39, drivers: PLIC, CLINT, UART
4. **BusyBox** (U-Mode) — shell and userspace utilities

## Build

```bash
# Requirements: JDK 21, Mill 0.11.7, firtool 1.144.0, Verilator 5.x
# RISC-V toolchains: riscv64-unknown-elf-gcc, riscv64-linux-gnu-gcc

# Install Mill
curl -L https://github.com/com-lihaoyi/mill/releases/download/0.11.7/mill > mill
chmod +x mill && sudo mv mill /usr/local/bin/mill

# Clone with submodules
git clone --recursive https://github.com/engmateusnebias-ship-it/rv64-soc-platform.git

# Compile
mill soc.compile

# Verify SoC configuration
mill soc.runMain config.RV64SoCTop

# Run Cocotb integration tests (Phase 3)
make -C verif/cocotb

# Run UVM regression (Phase 7)
make -C verif/uvm
```

## Status

| Phase | Description                        | Status         |
|-------|------------------------------------|----------------|
| 1     | Environment setup                  | ✅ Complete    |
| 2     | Rocket Chip + Mill + CI            | ✅ Complete    |
| 3     | Verilator + Cocotb smoke tests     | 🔄 In progress |
| 4     | Peripherals + Device Tree          | ⬜ Planned     |
| 5     | OpenSBI boot                       | ⬜ Planned     |
| 6     | Linux + BusyBox boot               | ⬜ Planned     |
| 7     | UVM TileLink verification          | ⬜ Planned     |
| 8     | riscv-formal ISA checking          | ⬜ Planned     |

## License

MIT
