/**
 * Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wudaosoft.laodongbuzhu.utils;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author changsoul.wu
 */
public class EncryptUtil {

    public static String encrypt(String key, String password, Context context)
            throws ScriptException, IOException, NoSuchMethodException {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        InputStream is = context.getAssets().open("encrypt.js");
        Reader reader = new InputStreamReader(is);

        try {
            engine.eval(reader);
            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;

                String pwd = (String) invoke.invokeFunction("encrypt", key, password);

                return (String) invoke.invokeFunction("stringToHex", pwd);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }

        throw new ScriptException("javascript file is not Invocable.");
    }
}
