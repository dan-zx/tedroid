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
package mx.udlap.is522.tedroid.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.util.Typefaces;

/**
 * Fragmento que representa el layout de instrucciones.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class InstructionsFragment extends Fragment {

    private static final int NOT_AN_ID = -1;
    private int pageNumber;

    /**
     * @param pageNumber el número de página actual. 
     * @return un nuevo InstructionsFragment.
     */
    public static InstructionsFragment create(int pageNumber) {
        InstructionsFragment fragment = new InstructionsFragment();
        fragment.pageNumber = pageNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_instructions, container, false);
        ImageView instructionImage = (ImageView) rootView.findViewById(R.id.instruction_image);
        TextView instructionText = (TextView) rootView.findViewById(R.id.instruction_text);
        int instructionImageContentDescriptionId = getTextByPageNumber();
        if (instructionImageContentDescriptionId != NOT_AN_ID) {
            Typeface typeface = Typefaces.get(getActivity(), Typefaces.Font.TWOBIT);
            instructionText.setText(instructionImageContentDescriptionId);
            instructionText.setTypeface(typeface);
            instructionImage.setContentDescription(getString(instructionImageContentDescriptionId));
        }

        int instructionImageDrawableId = getDrawableByPageNumber();
        if (instructionImageDrawableId != NOT_AN_ID) instructionImage.setImageResource(instructionImageDrawableId);
        return rootView;
    }

    /**
     * @return el id del text según la página seleccionada o {@value #NOT_AN_ID} si no es una página
     *         válida.
     */
    private int getTextByPageNumber() {
        switch (pageNumber) {
            case 0: return R.string.drag_instruction_text;
            case 1: return R.string.long_press_instruction_text;
            case 2: return R.string.single_tap_instruction_text;
            default: return NOT_AN_ID;
        }
    }

    /**
     * @return el id del drawable según la página seleccionada o {@value #NOT_AN_ID} si no es una
     *         página válida.
     */
    private int getDrawableByPageNumber() {
        switch (pageNumber) {
            case 0: return R.drawable.drag;
            case 1: return R.drawable.hard_drop;
            case 2: return R.drawable.rotate;
            default: return NOT_AN_ID;
        }
    }
}