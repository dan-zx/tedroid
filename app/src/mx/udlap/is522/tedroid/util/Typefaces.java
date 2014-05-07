/*
 * Copyright 2014 Tedroid developers
 *
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
 */
package mx.udlap.is522.tedroid.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Metodos y constantes genéricas de la clase Typeface.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Typefaces {

    /**
     * Paths de la fuentes usadas en la aplicación.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum Font {
        TWOBIT ("fonts/twobit.ttf");

        private final String path;

        Font(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /** NO INVOCAR. */
    private Typefaces() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param context el contexto de la aplicación.
     * @param which que fuente.
     * @return una fuente que se encuentra en assets
     */
    public static Typeface get(Context context, Font which) {
        return Typeface.createFromAsset(context.getAssets(), which.path);
    }
}