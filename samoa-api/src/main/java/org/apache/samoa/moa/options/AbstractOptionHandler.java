package org.apache.samoa.moa.options;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2014 - 2015 Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.samoa.moa.AbstractMOAObject;
import org.apache.samoa.moa.core.ObjectRepository;
import org.apache.samoa.moa.tasks.NullMonitor;
import org.apache.samoa.moa.tasks.TaskMonitor;

import com.github.javacliparser.Options;

/**
 * Abstract Option Handler. All classes that have options in MOA extend this class.
 * 
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public abstract class AbstractOptionHandler extends AbstractMOAObject implements
    OptionHandler {

  private static final long serialVersionUID = 1L;

  /** Options to handle */
  // protected Options options;

  /** Dictionary with option texts and objects */
  // protected Map<String, Object> classOptionNamesToPreparedObjects;

  @Override
  public String getPurposeString() {
    return "Anonymous object: purpose undocumented.";
  }

  @Override
  public Options getOptions() {
    /*
     * if (this.options == null) { this.options = new Options(); if
     * (this.config== null) { this.config = new OptionsHandler(this, "");
     * this.config.prepareForUse(); } Option[] myOptions =
     * this.config.discoverOptionsViaReflection(); for (Option option :
     * myOptions) { this.options.addOption(option); } } return this.options;
     */
    if (this.config == null) {
      this.config = new OptionsHandler(this, "");
      // this.config.prepareForUse(monitor, repository);
    }
    return this.config.getOptions();
  }

  @Override
  public void prepareForUse() {
    prepareForUse(new NullMonitor(), null);
  }

  protected OptionsHandler config;

  @Override
  public void prepareForUse(TaskMonitor monitor, ObjectRepository repository) {
    // prepareClassOptions(monitor, repository);
    if (this.config == null) {
      this.config = new OptionsHandler(this, "");
      this.config.prepareForUse(monitor, repository);
    }
    prepareForUseImpl(monitor, repository);
  }

  /**
   * This method describes the implementation of how to prepare this object for use. All classes that extends this class
   * have to implement <code>prepareForUseImpl</code> and not <code>prepareForUse</code> since
   * <code>prepareForUse</code> calls <code>prepareForUseImpl</code>.
   * 
   * @param monitor
   *          the TaskMonitor to use
   * @param repository
   *          the ObjectRepository to use
   */
  protected abstract void prepareForUseImpl(TaskMonitor monitor,
      ObjectRepository repository);

  @Override
  public String getCLICreationString(Class<?> expectedType) {
    return ClassOption.stripPackagePrefix(this.getClass().getName(),
        expectedType)
        + " " + getOptions().getAsCLIString();
  }

  @Override
  public OptionHandler copy() {
    return (OptionHandler) super.copy();
  }

  /**
   * Gets the options of this class via reflection.
   * 
   * @return an array of options
   */
  /*
   * protected Option[] discoverOptionsViaReflection() { Class<? extends
   * AbstractOptionHandler> c = this.getClass(); Field[] fields = c.getFields();
   * List<Option> optList = new LinkedList<Option>(); for (Field field : fields)
   * { String fName = field.getName(); Class<?> fType = field.getType(); if
   * (fType.getName().endsWith("Option")) { if
   * (Option.class.isAssignableFrom(fType)) { Option oVal = null; try {
   * field.setAccessible(true); oVal = (Option) field.get(this); } catch
   * (IllegalAccessException ignored) { // cannot access this field } if (oVal
   * != null) { optList.add(oVal); } } } } return optList.toArray(new
   * Option[optList.size()]); }
   */

  /**
   * Prepares the options of this class.
   * 
   * @param monitor
   *          the TaskMonitor to use
   * @param repository
   *          the ObjectRepository to use
   */
  protected void prepareClassOptions(TaskMonitor monitor,
      ObjectRepository repository) {
    this.config.prepareClassOptions(monitor, repository);
  }/*
    * this.classOptionNamesToPreparedObjects = null; Option[] optionArray =
    * getOptions().getOptionArray(); for (Option option : optionArray) { if
    * (option instanceof ClassOption) { ClassOption classOption = (ClassOption)
    * option; monitor.setCurrentActivity("Materializing option " +
    * classOption.getName() + "...", -1.0); Object optionObj =
    * classOption.materializeObject(monitor, repository); if
    * (monitor.taskShouldAbort()) { return; } if (optionObj instanceof
    * OptionHandler) { monitor.setCurrentActivity("Preparing option " +
    * classOption.getName() + "...", -1.0); ((OptionHandler)
    * optionObj).prepareForUse(monitor, repository); if
    * (monitor.taskShouldAbort()) { return; } } if
    * (this.classOptionNamesToPreparedObjects == null) {
    * this.classOptionNamesToPreparedObjects = new HashMap<String, Object>(); }
    * this.classOptionNamesToPreparedObjects.put(option.getName(), optionObj); }
    * } }
    */

  /**
   * Gets a prepared option of this class.
   * 
   * @param opt
   *          the class option to get
   * @return an option stored in the dictionary
   */
  protected Object getPreparedClassOption(ClassOption opt) {
    return this.config.getPreparedClassOption(opt);
  }
}
