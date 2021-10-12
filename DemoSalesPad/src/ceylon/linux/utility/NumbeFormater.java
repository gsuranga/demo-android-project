
package ceylon.linux.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumbeFormater {


	public static String FormatIntegerToDecimal(int number) {
		DecimalFormat df = new DecimalFormat("#.00");
		String angleFormated = df.format(number);
		return angleFormated;
	}

	public static String round(String value, int places) {
		if (places < 0) throw new IllegalArgumentException();


		BigDecimal bd = new BigDecimal(value);


		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return String.valueOf(bd.doubleValue());
	}
	
	public String format_double_val_to_decimal_Strng(double number)
	{
		String a="0.00";
		if(number==0)
		{
			
		}
		else
		{
		    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		    a= formatter.format(number);
		}
		return a;
	}
}
