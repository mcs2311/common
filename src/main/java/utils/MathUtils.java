//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.nio.*;
import java.util.*;
import java.math.*;
import java.text.*;

//import org.apache.commons.math3.analysis.function.*;

//-------------------------------------------------------------------------------------
public class MathUtils {

//-------------------------------------------------------------------------------------
    public static final int tenToPower(int power) {
	    int result = 1;
	    for (int p = 0; p < power; p++){
	        result *= 10;
	    }
	    return result;
	} 

//-------------------------------------------------------------------------------------
    public static final float variation(float _newValue, float _oldValue) {
//    	float _difference = Math.abs(_v2 - _v1);
    	float _difference = _oldValue - _newValue;
    	float _variation = (_difference * 100f) / _oldValue;
    	return _variation;
	}

//https://stats.stackexchange.com/questions/70801/how-to-normalize-data-to-0-1-range
//-------------------------------------------------------------------------------------
    public static final float normalize(float _x) {
		return (1f / (1f + (_x * 10)));
	}

//-------------------------------------------------------------------------------------
    public static final float normalize(float _n, float _x) {
		return (_n / (1f + (_x * 10)));
	}

//-------------------------------------------------------------------------------------
    public static final float normalize_Infinity0(float _x) {
		return (1f / (1f + _x));
	}

//-------------------------------------------------------------------------------------
    public static final float normalize_01(float _value, float _min, float _max) {
		return (_value - _min) / (_max - _min);
	}

//-------------------------------------------------------------------------------------
    public static final float normalize_MinMax(float _value, float _min, float _max, float _minNew, float _maxNew) {
		return ((_maxNew - _minNew) / (_max - _min) * (_value - _max)) + _maxNew;
	}

//-------------------------------------------------------------------------------------
    public static final float normalize_01f(float _value, float _max) {
		float _result = ((1f / _max) * (_value - _max)) + 1f;
//		System.out.println(">>>normalize_01f: _value=" + _value + ", _max="+_max + ", _result="+_result);
		return _result;
	}

//-------------------------------------------------------------------------------------
	public static final int denormalize(float _normalized, int _min, int _max) {
		int _denormalized = (int)(_normalized * (_max - _min) + _min);
		return _denormalized;
	}

//-------------------------------------------------------------------------------------
    public static final float profit(float _currentPrice, float _openingPrice) {
    	float _profit;
        if(_openingPrice != (float)0.0){
	        _profit = variation(_currentPrice, _openingPrice);
        } else {
        	_profit = (float)0.0;
        }
        return _profit;
    }

//-------------------------------------------------------------------------------------
	public static final double getMantissa(double x) {
	    int exponent = Math.getExponent(x);
	    return x / Math.pow(2, exponent);
	}

//-------------------------------------------------------------------------------------
	public static final double round(double value) {
	 	BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public static final float decrementValue(float _value, float _ratio, final float _min){
		float _percent = (_value * _ratio);
		_value = _value - _percent;
		if(_value < _min){
			_value = _min;
		}
		return _value;
	}

//-------------------------------------------------------------------------------------
	public static final float incrementValue(float _value, float _ratio, final float _max){
		float _percent = (_value * _ratio);
		_value = _value + _percent;
		if(_value > _max){
			_value = _max;
		}
		return _value;
	}

//-------------------------------------------------------------------------------------
	public static final float slowDecrementValue(float _value, float _ratio, final float _min){
		_ratio = flatten(_ratio);
		return decrementValue(_value, _ratio, _min);
	}

//-------------------------------------------------------------------------------------
	public static final float slowIncrementValue(float _value, float _ratio, final float _max){
		_ratio = flatten(_ratio);
		return incrementValue(_value, _ratio, _max);
	}

//-------------------------------------------------------------------------------------
	public static final float flatten(float _value){
		_value = (float)Math.expm1(_value);
		return normalize_01f(_value, (float)Math.E - 1);
	}


//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------

   