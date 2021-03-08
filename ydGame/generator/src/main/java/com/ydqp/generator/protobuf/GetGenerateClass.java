package com.ydqp.generator.protobuf;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.cfq.annotation.ClassParse;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.annotation.ServerHandler;
import com.cfq.handler.IServerHandler;
import com.cfq.handler.ManagerHandler;
import com.cfq.handler.ManagerModule;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.ManageParaseMessage;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetGenerateClass {

    public static void loadPackage (String packages) {

        String path = "D:\\yanfa\\youxi\\qp\\genPro";
        String[] p = new String[]{packages};
        Map<String, String> genFile = new HashMap<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            ClassParse classParse = new ClassParse();
            List<String> classes = classParse.parse(p);
            for (String cls : classes) {
                Class clazz = classLoader.loadClass(cls);
                if (clazz.isAnnotationPresent(GenProto.class)) {
                    GenProto genProto = (GenProto) clazz.getAnnotation(GenProto.class);
                    if (genProto != null) {
                        String fileName = genProto.modulePro();
                        String code = ProtobufIDLGenerator.getIDL(clazz);

                        String[] strings = code.split("\n");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String s :  strings) {
                            if (s.contains("syntax=") || s.contains("package com.ydqp.common")
                            || s.contains("option java_outer_classname =")) {
                                continue;
                            }
                            stringBuilder.append(s +"\n");
                        }

                        String oldCode = genFile.get(fileName);
                        if (oldCode == null) {
                            String ss = "syntax=\"proto3\";\n" + "package com.ydqp.common.receiveProtoMsg;\n" +
                                "option java_outer_classname = \"" + fileName +"\";\n" ;
                            genFile.put(fileName, ss + stringBuilder.toString());
                        } else {
                            genFile.put(fileName, oldCode + stringBuilder.toString());
                        }
                    }
                }

            }

            for (Map.Entry<String, String> entry : genFile.entrySet()) {
                FileWriter entityNameWriter = null;
                entityNameWriter = new FileWriter(path + File.separator + entry.getKey()+".proto");
                entityNameWriter.write(entry.getValue());
                entityNameWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
