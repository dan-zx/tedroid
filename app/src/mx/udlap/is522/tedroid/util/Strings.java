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

/**
 * Metodos y constantes genéricas de la clase String.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Strings {

    /** Un objeto String vacio. */
    public static final String EMPTY = "";

    /** NO INVOCAR. */
    private Strings() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param str el objeto String a provar.
     * @return si es o no vacio (sin texto con espacios) o {@code null}.
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }
}