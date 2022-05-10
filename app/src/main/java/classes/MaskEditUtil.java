package classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MaskEditUtil {

   public static final String FORMAT_FONE = "(##) ####-####";
   public static final String FORMAT_CELL = "(##) # ####-####";
   private static String mask = FORMAT_FONE;

   public static TextWatcher mask(final EditText ediTxt) {
       return new TextWatcher() {
           boolean isUpdating;
           String old = "";

           @Override
           public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

           @Override
           public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
               final String str = MaskEditUtil.unmask(s.toString());
               String mascara = "";
               if (isUpdating) {
                   old = str;
                   isUpdating = false;
                   return;
               }
               int i = 0;
               for (final char m : mask.toCharArray()) {
                   if (m != '#' && str.length() > old.length()) {
                       mascara += m;
                       continue;
                   }
                   try {
                       mascara += str.charAt(i);
                   } catch (final Exception e) {
                       break;
                   }
                   i++;
               }
               isUpdating = true;
               ediTxt.setText(mascara);
               ediTxt.setSelection(mascara.length());
           }

           @Override
           public void afterTextChanged(Editable editable) {
               if(ediTxt.getText().length() > 13){
                   mask = FORMAT_CELL;
               }else {
                mask = FORMAT_FONE;
               }
           }
       };
   }

   public static String unmask(final String s) {
       return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
   }
}
