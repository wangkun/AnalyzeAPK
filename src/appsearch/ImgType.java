/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package appsearch;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum ImgType implements org.apache.thrift.TEnum {
  ICON(0),
  DETAIL_IMAGE(1);

  private final int value;

  private ImgType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ImgType findByValue(int value) { 
    switch (value) {
      case 0:
        return ICON;
      case 1:
        return DETAIL_IMAGE;
      default:
        return null;
    }
  }
}