package org.keidan.bconv;

public enum Unit {
  Bit("bit", 1.0, 0), 
  Kb("Kb", 1024.0, 1), 
  Mb("Mb", Kb.value * Kb.value, 2), 
  Gb("Gb", Mb.value * Kb.value, 3), 
  Tb("Tb", Gb.value * Kb.value, 4),

  Octet("octet", Bit.value, 0), 
  Ko("Ko", Kb.value, 1), 
  Mo("Mo", Mb.value, 2), 
  Go("Go", Gb.value, 3), 
  To("To", Tb.value, 4);

  private double value;
  private String name;
  private int    index;

  Unit(final String name, final double value, final int index) {
    this.name = name;
    this.value = value;
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return name;
  }

  public double getValue() {
    return value;
  }
}
