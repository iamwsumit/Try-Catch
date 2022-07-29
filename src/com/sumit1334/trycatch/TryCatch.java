package com.sumit1334.trycatch;

import android.util.Log;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.lists.LList;
import gnu.mapping.ProcedureN;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import kawa.standard.Scheme;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class TryCatch extends AndroidNonvisibleComponent {
    private final String LOG_TAG = "TryCatch";
    private final Form form;
    private String lastId = "";

    public TryCatch(ComponentContainer container) {
        super(container.$form());
        this.form = container.$form();
    }

    @SimpleEvent
    public void Catch(String id, String exception) {
        EventDispatcher.dispatchEvent(this, "Catch", id, exception);
    }

    @SimpleEvent
    public void Exception(String id, String exceptionName, String errorFrom, String error) {
        EventDispatcher.dispatchEvent(this, "Exception", id, exceptionName, error);
    }

    @SimpleFunction
    public Object SelectListItem(String id, YailList list, int index) {
        try {
            return list.get(index);
        } catch (Exception exception) {
            this.Catch(id, exception.toString());
            return exception.toString();
        }
    }

    @SimpleFunction
    public Object CallMethod(Component component, String id, String methodName, YailList values) {
        try {
            Class klass = component.getClass();
            Object[] params = values.toArray();
            for (Method method : klass.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == params.length) {
                    ArrayList<Object> args = new ArrayList<>();
                    for (int i = 0; i < method.getParameterCount(); i++) {
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
                    Object value = method.invoke(component, args.toArray());
                    if (value != null)
                        return value;
                    else
                        return "";
                }
            }
        } catch (Exception e) {
            this.Catch(id, e.toString());
            Exception(id, e.getClass().getSimpleName(), component.getClass().getSimpleName() + "." + methodName, e.getMessage());
        }
        Catch(id, "No method found with this name in given component");
        return "";
    }

    @SimpleFunction
    public Object CallProcedure(String id, String procedureName, YailList params) {
        try {
            lastId = id;
            return doCall(procedureName, params);
        } catch (Exception e) {
            Catch(id, e.toString());
            Exception(id, e.getClass().getSimpleName(), procedureName, e.getMessage());
            return "";
        }
    }

    private ProcedureN lookupProcedureInRepl(String procedureName) {
        Scheme lang = Scheme.getInstance();
        try {
            Object result = lang.eval("(begin (require <com.google.youngandroid.runtime>)(get-var p$" +
                    procedureName + "))");
            if (result instanceof ProcedureN) {
                return (ProcedureN) result;
            } else {
                Log.e(LOG_TAG, "Wanted a procedure, but got a " +
                        (result == null ? "null" : result.getClass().toString()));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private ProcedureN lookupProcedureInForm(String procedureName) {
        try {
            Field globalVarEnvironment = form.getClass().getField("global$Mnvars$Mnto$Mncreate");
            LList vars = (LList) globalVarEnvironment.get(form);
            Symbol procSym = new SimpleSymbol("p$" + procedureName);
            Object result = null;
            for (Object pair : vars) {
                if (!LList.Empty.equals(pair)) {
                    LList asPair = (LList) pair;
                    if (((Symbol) asPair.get(0)).getName().equals(procSym.getName())) {
                        result = asPair.get(1);
                        break;
                    }
                }
            }
            if (result instanceof ProcedureN) {
                return (ProcedureN) ((ProcedureN) result).apply0();
            } else {
                Log.e(LOG_TAG, "Wanted a procedure, but got a " +
                        (result == null ? "null" : result.getClass().toString()));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private ProcedureN lookupProcedure(String procedureName) {
        if (form instanceof ReplForm) {
            return lookupProcedureInRepl(procedureName);
        } else {
            return lookupProcedureInForm(procedureName);
        }
    }

    private Object doCall(final String procedureName, YailList arguments) {
        ProcedureN fun = lookupProcedure(procedureName);
        if (fun == null) {
            throw new IllegalArgumentError("Unable to locate procedure " + procedureName +
                    " in form " + form.toString());
        }
        try {
            if (arguments == null || fun.numArgs() == 0) {
                return fun.apply0();
            } else {
                Object[] argarray = new Object[arguments.size()];
                int i = 0;
                Iterator it = arguments.iterator();
                it.next();
                while (it.hasNext()) {
                    argarray[i++] = it.next();
                }
                return fun.applyN(argarray);
            }
        } catch (final Throwable throwable) {
            form.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Exception(lastId, throwable.getClass().getSimpleName(), procedureName, throwable.getMessage());
                }
            });
            throwable.printStackTrace();
            return "";
        }
    }
}