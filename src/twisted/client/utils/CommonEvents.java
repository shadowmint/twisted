/**
 * Copyright 2010 Douglas Linder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twisted.client.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import twisted.client.events.ComponentChangeListener;
import twisted.client.events.ComponentClickListener;
import twisted.client.events.ComponentKeyListener;
import twisted.client.impl.ComponentQuery;
import twisted.client.ComponentContainer;
import twisted.client.ComponentRegister;
import twisted.client.Component;

/** Handle simple extremely common event types. */
public class CommonEvents {

  /** Event instances that have already been created. */
  private static HashMap<String, EventHandler> cache = new HashMap<String, EventHandler>();

  /** Callback cache. */
  private static HashMap<String, GenericCallback<?>> callbacks = new HashMap<String, GenericCallback<?>>();

  /** Prevents default form behavior for text inputs that submits form. */
  public static void preventFormSubmit(Element target) {
    ComponentKeyListener l = ComponentKeyListener.get(target);
    l.addKeyDownHandler(getPreventFormSubmitHandler());
  }

  /** Shows a display none element (style is set to block) */
  public static void show(Element target) {
    if (target != null) {
      String old = target.getAttribute("display");
      if ((old != null) && (old.equals("")))
          target.getStyle().setProperty("display", old);
      else
        target.getStyle().setDisplay(Display.BLOCK);
    }
  }

  /** Hides an element by setting display none. */
  public static void hide(Element target) {
    if (target != null) {
      target.setAttribute("display", target.getStyle().getDisplay());
      target.getStyle().setDisplay(Display.NONE);
    }
  }

  /** Disables an input element. */
  public static void disable(Element target) {
    if (target != null) {
      InputElement input = InputElement.as(target);
      if (input != null)
        input.setDisabled(true);
    }
  }

  /** Enables an input element. */
  public static void enable(Element target) {
    if (target != null) {
      InputElement input = InputElement.as(target);
      if (input != null)
        input.setDisabled(false);
    }
  }

  /** Fetches the text value of a form element. */
  public static String value(Element target) {
    String value = "";
    try {
      if (target != null) {
        if (target.getTagName().equalsIgnoreCase("Input")) {
          InputElement e = InputElement.as(target);
          value = e.getValue();
        }
        else if (target.getTagName().equalsIgnoreCase("TextArea")) {
          TextAreaElement e = TextAreaElement.as(target);
          value = e.getValue();
        }
        else if (target.getTagName().equalsIgnoreCase("Select")) {
          SelectElement e = SelectElement.as(target);
          value = e.getValue();
        }
      }
    }
    catch (Exception e) {
      // Possible we might have an element that casts badly.
      value = "";
    }
    return (value);
  }

  /** Sets the text value of a form element. */
  public static String value(Element target, String value) {
    try {
      if ((target != null) && (value != null)) {
        if (target.getTagName().equalsIgnoreCase("input")) {
          InputElement e = InputElement.as(target);
          e.setValue(value);
        }
        else if (target.getTagName().equalsIgnoreCase("TextArea")) {
          TextAreaElement e = TextAreaElement.as(target);
          e.setValue(value);
          value = e.getValue();
        }
        else if (target.getTagName().equalsIgnoreCase("Select")) {
          SelectElement e = SelectElement.as(target);
          e.setValue(value);
        }
      }
    }
    catch (Exception e) {
      // Possible we might have an element that casts badly.
      value = "";
    }
    return (value);
  }

  /** Returned key handler runs prevent default on enter presses. */
  private static KeyDownHandler getPreventFormSubmitHandler() {
    KeyDownHandler rtn = (KeyDownHandler) cache.get("preventFormSubmit");
    if (rtn == null) {
      rtn = new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            event.preventDefault();
        }
      };
      cache.put("preventFormSubmit", rtn);
    }
    return (rtn);
  }

  /** Binds a click listener */
  public static void attachClickListener(Element e, ClickHandler c) {
    ComponentClickListener listener = ComponentClickListener.get(e);
    listener.addClickHandler(c);
  }

  /** Binds a key listener */
  public static void attachKeyListener(Element e, KeyDownHandler k) {
    ComponentKeyListener listener = ComponentKeyListener.get(e);
    listener.addKeyDownHandler(k);
  }

  /** Binds a change listener */
  public static void attachChangeListener(Element e, ChangeHandler c) {
    ComponentChangeListener listener = ComponentChangeListener.get(e);
    listener.addChangeHandler(c);
  }

  /**
   * Attaches a shortcut key listener to trigger a callback.
   * <p>
   * If modifier is not one of KeyCodes.X, which X is KEY_SHIFT,
   * KEY_CTRL or KEY_ALT, the modifier is ignored. Use -1 or 0.
   * <p>
   * Note that for elements which are not selectable (ie. form items)
   * you may have to set the 'tabindex' property on the element to make
   * it focus'able in the browser before events will fire for it.
   * <p>
   * This is definitely the case for, for example, divs.
   * <p>
   * However, it's not possible to prevent default events from firing in
   * IE; at such the only way to use this is to bind a shortcut to an
   * un-used IE key binding. eg. Control-S.
   */
  public static void attachShortcutListener(Element e, char shortcut, int modifier, GenericCallback<KeyDownEvent> callback) {

    final char s = shortcut;
    final int m = modifier;
    final GenericCallback<KeyDownEvent> b = callback;

    CommonEvents.attachKeyListener(e, new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        char c = (char) event.getNativeKeyCode();
        c = Character.toLowerCase(c);
        boolean success = false;
        if (s == c) {
          if (m == KeyCodes.KEY_ALT) {
            if (event.isAltKeyDown())
              success = true;
          }
          else if (m == KeyCodes.KEY_CTRL) {
            if (event.isControlKeyDown())
              success = true;
          }
          else if (m == KeyCodes.KEY_SHIFT) {
            if (event.isShiftKeyDown())
              success = true;
          }
          else
            success = true;
        }
        if (success) {
          event.preventDefault();
          event.stopPropagation();
          b.onSuccess(event);
        }
      }
    });
  }

  /**
   * Attaches a widget to a generic element container.
   * <p>
   * The context is required to fetch the ComponentRegister; unlike other common
   * functions, that means this call must be invoked as:<br/>
   * CommonEvents.attachWidget(this, root.getAsset("..."), myWidget);
   */
  public static void attachWidget(Component context, Element e, Widget w) {
    // We could use a ComponentFrame here, but that isn't cached.
    ComponentRegister r = context.getContainer().getRegister();
    ComponentContainer c = ComponentContainer.get(e, r);
    c.add(w);
  }

  /**
   * Invokes a GenericCallback async without code splitting.
   * <p>
   * Really all we do is a 1ms delay on a timeout; but it queues things up and
   * lets them run when the browser is ready without locking.
   */
  public static void run(final GenericCallback<Void> callback) {
    if (callback != null) {
      Timer t = new Timer() {
        public void run() {
          callback.onSuccess(null);
        }
      };
      t.schedule(1);
    }
  }

  /**
   * Invokes a GenericCallback async without code splitting to throw an error.
   * <p>
   * Really all we do is a 1ms delay on a timeout; but it queues things up and
   * lets them run when the browser is ready without locking.
   */
  public static void run(final GenericCallback<Void> callback,
      final Throwable caught) {
    if (callback != null) {
      Timer t = new Timer() {
        public void run() {
          callback.onFailure(caught);
        }
      };
      t.schedule(1);
    }
  }

  /**
   * Creates a generic callback that might get reused.
   * <p>
   * This is just a helper function to reduce the number of callbacks created in tight
   * loops, where the content of the callback never changes.
   * <p>
   * Remember that callbacks created this way are cached for ever, and that the id's
   * are unique.
   * <p>
   * The call will return the callback with the given id if callback is null, otherwise
   * it will set the callback at that id and return it.
   * <p>
   * ...this way you can use a ? b : c to only create the callback once, when you need it.
   */
  public static GenericCallback<?> callback(String id, GenericCallback<?> callback) {
    if (callback != null)
      callbacks.put(id, callback);
    GenericCallback<?> rtn = callbacks.get(id);
    return(rtn);
  }

  /**
   * Populates an element with other elements via style tags.
   * <p>
   * Simple CSS factory class that takes an element with content
   * in the form ... <blah class="foo"></blah> ... and injects
   * the content from the properties object into all the instances
   * of "foo".
   * <p>
   * Note that the innerHTML of the matching object in properties
   * is injected, <i>not</i> the element itself, which allows
   * multiple injections, and that a single map be kept as a
   * template.
   * <p>
   * Note also that the innerHTML of the target is replaced, so
   * the use of inner spans may be appropriate for injecting text
   * in some cases.
   */
  public static void cssTemplate(Element template, Map<String,Element> properties) {
    for (String key : properties.keySet()) {
      ComponentQuery q = ComponentQuery.query(key, template);
      int size = q.getLength();
      if (size > 0) {
        Element c = properties.get(key);
        String content = "";
        if (c != null) {
          // IE will crap itself here if we use an existing div here. This isn't
          // the best way of doing this, but now it works in IE too...
          Element cssTemplate = Document.get().createDivElement();
          cssTemplate.appendChild(c);
          content = cssTemplate.getInnerHTML();
        }
        for (int i = 0; i < size; ++i) {
          Element target = q.getItem(i);
          target.setInnerHTML(content);
          target.removeClassName(key);
        }
      }
    }
  }
}
