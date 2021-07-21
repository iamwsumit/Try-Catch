package com.sumit1334.trycatch;

import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.YailList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TryCatch extends AndroidNonvisibleComponent {

  public TryCatch(ComponentContainer container) {
    super(container.$form());
  }

  @SimpleEvent
  public void Catch(String exception){
    EventDispatcher.dispatchEvent(this,"Catch",exception);
  }
  @SimpleFunction
  public Object SelectListItem(YailList list,int index){
    try {
      return list.get(index);
    }catch (Exception exception){
      this.Catch(exception.toString());
      return null;
    }
  }
  @SimpleFunction
  public Object CallMethod(Component component,String methodName,YailList values){
    Object value=null;
    try {
    Class klass=component.getClass();
    Object[] params= values.toArray();
    for (Method method:klass.getMethods()) {
      if (method.getName().equals(methodName) && method.getParameterCount() == params.length){
        ArrayList<Object> args=new ArrayList<>();
        for (int i=0;i<method.getParameterCount();i++){
          if (method.getParameterTypes()[i].getName().equals(String.class.getName()))
            args.add(params[i].toString());
          else if (method.getParameterTypes()[i].getName().equals(int.class.getName()))
            args.add(Integer.parseInt(params[i].toString()));
          else if (method.getParameterTypes()[i].getName().equals(boolean.class.getName()))
            args.add(Boolean.parseBoolean(params[i].toString()));
          else if (method.getParameterTypes()[i].getName().equals(float.class.getName()))
            args.add(Float.parseFloat(params[i].toString()));
          else if (method.getParameterTypes()[i].getName().equals(double.class.getName()))
            args.add(Double.parseDouble(params[i].toString()));
          else {
            args.add(params[i]);
          }
        }
        value= method.invoke(component,args.toArray());
      }
    }
    } catch (IllegalAccessException e) {
      this.Catch(e.toString());
    } catch (InvocationTargetException e) {
      this.Catch(e.toString());
    } catch (Exception e){
      this.Catch(e.toString());
    } finally {
      if (value!=null)
        return value;
      else
        return null;
    }
  }
}
