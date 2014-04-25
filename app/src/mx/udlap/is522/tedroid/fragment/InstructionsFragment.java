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
        instructionImage.setContentDescription(getString(getTextByPageNumber()));
        instructionImage.setImageResource(getDrawableByPageNumber());
        TextView instructionText = (TextView) rootView.findViewById(R.id.instruction_text);
        Typeface typeface = Typefaces.get(getActivity(), Typefaces.Font.TWOBIT);
        instructionText.setText(getTextByPageNumber());
        instructionText.setTypeface(typeface);
        return rootView;
    }

    /** @return el id del text según la página seleccionada. */
    private int getTextByPageNumber() {
        switch (pageNumber) {
            case 0: return R.string.drag_instruction_text;
            case 1: return R.string.long_press_instruction_text;
            case 2: return R.string.single_tap_instruction_text;
            default: return -1;
        }
    }

    /** @return el id del drawable según la página seleccionada. */
    private int getDrawableByPageNumber() {
        switch (pageNumber) {
            case 0: return R.drawable.drag;
            case 1: return R.drawable.hard_drop;
            case 2: return R.drawable.rotate;
            default: return -1;
        }
    }
}