package com.orbitz.monitoring.api.monitor;

import com.orbitz.monitoring.api.Attribute;
import com.orbitz.monitoring.api.Monitor;
import com.orbitz.monitoring.api.MonitoringEngine;
import com.orbitz.monitoring.api.MonitoringLevel;
import com.orbitz.monitoring.api.monitor.serializable.SerializableMonitor;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.CharSetUtils;
import org.apache.log4j.Logger;

/**
 * A base implementation of {@link Monitor} that contains all common aspects of monitors.
 * 
 * @author Doug Barth
 */
public abstract class AbstractMonitor implements Monitor {
  
  private static final Logger log = Logger.getLogger(AbstractMonitor.class);
  
  protected AttributeMap attributes;
  private boolean processed;
  protected MonitoringLevel monitoringLevel = MonitoringLevel.INFO;
  
  private static final String invalidCharacters = " \\[\\]*,|()$@|~?&<>\\^";
  private static final Set invalidCharSet = buildInvalidCharSet();
  
  // ** CONSTRUCTORS ********************************************************
  
  /**
   * Initializes the attribute map and global attributes. Subclasses will need to call init(String)
   * themselves.
   */
  protected AbstractMonitor() {
    attributes = createAttributeMap();
    processed = false;
  }
  
  /**
   * Initializes the attribute map, global attributes and calls init(String).
   * 
   * @param name the name of the monitor
   */
  public AbstractMonitor(final String name) {
    this(name, MonitoringLevel.INFO, null);
  }
  
  /**
   * Initializes the attribute map, global attributes, monitoring level and calls init(String).
   * 
   * @param name the name of the monitor
   * @param monitoringLevel the monitoring level
   */
  public AbstractMonitor(final String name, final MonitoringLevel monitoringLevel) {
    this(name, monitoringLevel, null);
  }
  
  /**
   * Initializes the attribute map, global attributes, sets the provided inherited attributes and
   * calls init(String).
   * 
   * @param name the name of the monitor
   * @param inheritedAttributes the collection of inherited attributes
   */
  public AbstractMonitor(final String name, final Map inheritedAttributes) {
    this(name, MonitoringLevel.INFO, inheritedAttributes);
  }
  
  /**
   * Initializes the attribute map, global attributes, monitoring level, sets the provided inherited
   * attributes and calls init(String).
   * 
   * @param name the name of the monitor
   * @param monitoringLevel the monitoring level
   * @param inheritedAttributes the collection of inherited attributes
   */
  public AbstractMonitor(final String name, final MonitoringLevel monitoringLevel,
      final Map<String, Object> inheritedAttributes) {
    this();
    this.monitoringLevel = monitoringLevel;
    init(name, inheritedAttributes);
  }
  
  // ** PUBLIC METHODS ******************************************************
  public AttributeHolder set(final String key, final short value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final int value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final long value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final float value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final double value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final char value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final byte value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final boolean value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final Date value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final String value) {
    return attributes.set(key, value).serializable();
  }
  
  public AttributeHolder set(final String key, final Object value) {
    return attributes.set(key, value);
  }
  
  public void setAll(final Map attributes) {
    this.attributes.setAll(attributes);
  }
  
  public void setAllAttributeHolders(final Map attributeHolders) {
    attributes.setAllAttributeHolders(attributeHolders);
  }
  
  public void unset(final String key) {
    attributes.unset(key);
  }
  
  public Object get(final String key) {
    return attributes.get(key);
  }
  
  public Map getAsMap(final String key) {
    return attributes.getAsMap(key);
  }
  
  public List getAsList(final String key) {
    return attributes.getAsList(key);
  }
  
  public Set getAsSet(final String key) {
    return attributes.getAsSet(key);
  }
  
  public String getAsString(final String key) {
    return attributes.getAsString(key);
  }
  
  public short getAsShort(final String key) {
    return attributes.getAsShort(key);
  }
  
  public int getAsInt(final String key) {
    return attributes.getAsInt(key);
  }
  
  public long getAsLong(final String key) {
    return attributes.getAsLong(key);
  }
  
  public float getAsFloat(final String key) {
    return attributes.getAsFloat(key);
  }
  
  public double getAsDouble(final String key) {
    return attributes.getAsDouble(key);
  }
  
  public char getAsChar(final String key) {
    return attributes.getAsChar(key);
  }
  
  public byte getAsByte(final String key) {
    return attributes.getAsByte(key);
  }
  
  public boolean getAsBoolean(final String key) {
    return attributes.getAsBoolean(key);
  }
  
  public Map getAll() {
    return attributes.getAll();
  }
  
  public Map getAllSerializable() {
    return attributes.getAllSerializable();
  }
  
  public boolean getAsBoolean(final String key, final boolean defaultValue) {
    return attributes.getAsBoolean(key, defaultValue);
  }
  
  public short getAsShort(final String key, final short defaultValue) {
    return attributes.getAsShort(key, defaultValue);
  }
  
  public byte getAsByte(final String key, final byte defaultValue) {
    return attributes.getAsByte(key, defaultValue);
  }
  
  public int getAsInt(final String key, final int defaultValue) {
    return attributes.getAsInt(key, defaultValue);
  }
  
  public long getAsLong(final String key, final long defaultValue) {
    return attributes.getAsLong(key, defaultValue);
  }
  
  public float getAsFloat(final String key, final float defaultValue) {
    return attributes.getAsFloat(key, defaultValue);
  }
  
  public double getAsDouble(final String key, final double defaultValue) {
    return attributes.getAsDouble(key, defaultValue);
  }
  
  public char getAsChar(final String key, final char defaultValue) {
    return attributes.getAsChar(key, defaultValue);
  }
  
  public final MonitoringLevel getLevel() {
    final MonitoringLevel overrideLevel = MonitoringEngine.getInstance()
        .getOverrideLevelForMonitor(this);
    return (overrideLevel != null ? overrideLevel : monitoringLevel);
  }
  
  public boolean hasAttribute(final String key) {
    return attributes.hasAttribute(key);
  }
  
  /**
   * Get a serializable version of this monitor.
   * 
   * @return the serializable monitor
   */
  public SerializableMonitor getSerializableMomento() {
    final MonitoringEngine engine = MonitoringEngine.getInstance();
    final Map serializableAttributes = engine.makeAttributeHoldersSerializable(attributes
        .getAllAttributeHolders());
    
    final SerializableMonitor monitor = new SerializableMonitor(null);
    monitor.setAllAttributeHolders(serializableAttributes);
    
    return monitor;
  }
  
  @Override
  public String toString() {
    final StringBuffer buf = new StringBuffer();
    
    buf.append("[").append(getClass()).append(" attributes=");
    buf.append(attributes);
    buf.append(" level=").append(monitoringLevel).append("]");
    
    return buf.toString();
  }
  
  // ** PROTECTED METHODS ***************************************************
  /**
   * Used to invoke the monitor lifecycle methods MonitoringEngine.initMonitor and
   * MonitoringEngine.monitorCreated on this monitor.
   * 
   * @param name the name of the monitor
   * @param inheritedAttributes the collection of inherited attributes
   */
  protected void init(String name, final Map<String, Object> inheritedAttributes) {
    MonitoringEngine.getInstance().initMonitor(this);
    if (name != null) {
      for (int i = 0; i < name.length(); i++) {
        if (invalidCharSet.contains(new Character(name.charAt(i)))) {
          name = CharSetUtils.delete(name, invalidCharacters);
          break;
        }
      }
    }
    set(Attribute.NAME, name);
    
    setInheritedAttributes(inheritedAttributes);
    
    MonitoringEngine.getInstance().monitorCreated(this);
  }
  
  /**
   * Used to set the inherited attributes on this monitor.
   * 
   * @param inheritedAttributes the collection of inherited attributes
   */
  protected void setInheritedAttributes(final Map<String, Object> inheritedAttributes) {
    if (inheritedAttributes != null) {
      for (final Map.Entry<String, Object> entry : inheritedAttributes.entrySet()) {
        final String key = entry.getKey();
        Object value = entry.getValue();
        if (value != null && AttributeHolder.class.isAssignableFrom(value.getClass())) {
          value = ((AttributeHolder)value).getValue();
        }
        set(key, value).lock();
      }
    }
  }
  
  /**
   * Used to invoke the monitor lifecycle method MonitoringEngine.process on this monitor.
   */
  protected void process() {
    if (processed) {
      log.error("This monitor has already been processed: " + this);
    }
    else {
      MonitoringEngine.getInstance().process(this);
      processed = true;
    }
  }
  
  protected AttributeMap createAttributeMap() {
    return new AttributeMap();
  }
  
  protected AttributeMap getAttributes() {
    return attributes;
  }
  
  // ** PRIVATE Methods
  private static Set buildInvalidCharSet() {
    final Set set = new HashSet();
    final char[] invalidArr = invalidCharacters.toCharArray();
    for (int i = 0; i < invalidArr.length; i++) {
      set.add(new Character(invalidArr[i]));
    }
    return set;
  }
}
