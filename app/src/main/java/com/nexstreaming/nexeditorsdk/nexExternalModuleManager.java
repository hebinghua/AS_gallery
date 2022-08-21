package com.nexstreaming.nexeditorsdk;

import com.nexstreaming.nexeditorsdk.module.UserField;
import com.nexstreaming.nexeditorsdk.module.nexExternalExportProvider;
import com.nexstreaming.nexeditorsdk.module.nexFaceDetectionProvider;
import com.nexstreaming.nexeditorsdk.module.nexModuleProvider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class nexExternalModuleManager {
    private static final String TAG = "nexModuleManager";
    private static final Class[] s_supportedModuleClass = {nexExternalExportProvider.class, nexFaceDetectionProvider.class};
    private static nexExternalModuleManager single;
    private List<a> moduleInfos = new ArrayList();
    private Map<String, Class<? extends nexModuleProvider>> moduleProviders = new HashMap();

    private nexExternalModuleManager() {
    }

    public static nexExternalModuleManager getInstance() {
        if (single == null) {
            single = new nexExternalModuleManager();
        }
        return single;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void registerModule(String str) {
        try {
            registerModule((Class<? extends nexModuleProvider>) Class.forName(str));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerModule(Class<? extends nexModuleProvider> cls) {
        Constructor<? extends nexModuleProvider> constructor;
        try {
            constructor = cls.getConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            constructor = null;
        }
        if (constructor != null) {
            try {
                nexModuleProvider newInstance = constructor.newInstance(new Object[0]);
                for (a aVar : this.moduleInfos) {
                    if (aVar.uuid().compareTo(newInstance.uuid()) == 0) {
                        return;
                    }
                }
                int subClassType = getSubClassType(newInstance);
                if (subClassType == 0) {
                    throw new RuntimeException("not supported Provider interface. uuid=" + newInstance.uuid());
                }
                this.moduleInfos.add(new a(newInstance, subClassType));
                this.moduleProviders.put(newInstance.uuid(), cls);
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InstantiationException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
    }

    public void unregisterModule(nexModuleProvider nexmoduleprovider) {
        unregisterModule(nexmoduleprovider.uuid());
    }

    public void unregisterModule(String str) {
        a aVar;
        Iterator<a> it = this.moduleInfos.iterator();
        while (true) {
            if (!it.hasNext()) {
                aVar = null;
                break;
            }
            aVar = it.next();
            if (aVar.uuid().compareTo(str) == 0) {
                break;
            }
        }
        if (aVar != null) {
            this.moduleInfos.remove(aVar);
            this.moduleProviders.remove(str);
        }
    }

    public void clearModule() {
        this.moduleInfos.clear();
        this.moduleProviders.clear();
    }

    /* loaded from: classes3.dex */
    public final class a implements nexModuleProvider {
        private final String b;
        private final String c;
        private final String d;
        private final String e;
        private final String f;
        private final int g;
        private final UserField[] h;
        private final int i;

        private a(nexModuleProvider nexmoduleprovider, int i) {
            this.b = nexmoduleprovider.name();
            this.c = nexmoduleprovider.uuid();
            this.d = nexmoduleprovider.description();
            this.e = nexmoduleprovider.auth();
            this.g = nexmoduleprovider.version();
            this.h = nexmoduleprovider.userFields();
            this.f = nexmoduleprovider.format();
            this.i = i;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public String name() {
            return this.b;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public String uuid() {
            return this.c;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public String description() {
            return this.d;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public String auth() {
            return this.e;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public String format() {
            return this.f;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public int version() {
            return this.g;
        }

        @Override // com.nexstreaming.nexeditorsdk.module.nexModuleProvider
        public UserField[] userFields() {
            return this.h;
        }
    }

    private int getSubClassType(Object obj) {
        int i = 0;
        int i2 = 0;
        while (true) {
            Class[] clsArr = s_supportedModuleClass;
            if (i < clsArr.length) {
                if (clsArr[i].isInstance(obj)) {
                    i2 |= i + 1;
                }
                i++;
            } else {
                return i2;
            }
        }
    }

    private int getSubClassType(Class<? extends nexModuleProvider> cls) {
        int i = 0;
        while (true) {
            Class[] clsArr = s_supportedModuleClass;
            if (i < clsArr.length) {
                if (clsArr[i].isAssignableFrom(cls)) {
                    return i + 1;
                }
                i++;
            } else {
                return 0;
            }
        }
    }

    public List<nexModuleProvider> getModules(Class<? extends nexModuleProvider> cls) {
        ArrayList arrayList = new ArrayList();
        int subClassType = getSubClassType(cls);
        for (a aVar : this.moduleInfos) {
            if ((aVar.i & subClassType) != 0) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    public Object getModule(String str, Class<? extends nexModuleProvider> cls) {
        int subClassType = getSubClassType(cls);
        for (a aVar : this.moduleInfos) {
            if (aVar.name().compareTo(str) == 0 && (aVar.i & subClassType) != 0) {
                return getModule(aVar.uuid());
            }
        }
        return null;
    }

    public Object getModule(String str) {
        Constructor<? extends nexModuleProvider> constructor;
        try {
            constructor = this.moduleProviders.get(str).getConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            constructor = null;
        }
        if (constructor != null) {
            try {
                return constructor.newInstance(new Object[0]);
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InstantiationException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return null;
    }
}
