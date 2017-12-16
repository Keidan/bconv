package fr.ralala.bconv;


/**
 *******************************************************************************
 * <p><b>Project bconv</b><br/>
 * Management of units
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public enum Unit {
  BIT("bit", 1.0, 0),
  KB("Kb", 1024.0, 1),
  MB("Mb", KB.mValue * KB.mValue, 2),
  GB("Gb", MB.mValue * KB.mValue, 3),
  TB("Tb", GB.mValue * KB.mValue, 4),

  OCTET("octet", BIT.mValue, 0),
  KO("Ko", KB.mValue, 1),
  MO("Mo", MB.mValue, 2),
  GO("Go", GB.mValue, 3),
  TO("To", TB.mValue, 4);

  private double mValue;
  private String mName;
  private int mIndex;

  /**
   * Creates the enum.
   * @param name The enum label.
   * @param value The associated value.
   * @param index The unique index.
   */
  Unit(final String name, final double value, final int index) {
    mName = name;
    mValue = value;
    mIndex = index;
  }

  /**
   * Returns an unique index.
   * @return int
   */
  public int index() {
    return mIndex;
  }

  /**
   * Returns the name.
   * @return String
   */
  @Override
  public String toString() {
    return mName;
  }

  /**
   * Returns the associated value.
   * @return double
   */
  public double value() {
    return mValue;
  }
}
