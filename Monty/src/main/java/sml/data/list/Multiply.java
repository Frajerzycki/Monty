package sml.data.list;

import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

final class Multiply extends Method<List> {
    Multiply(List parent) {
        super(parent, "$a_mul",new String[2]);
        addParameter("this");
        addParameter("times");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var _times = getBody().getVariableValue("times", callFileName, callLine);
        int times = 0;
        if (_times instanceof Integer)
            times = (int) _times;
        else if (_times instanceof BigInteger) {
            var bigTimes = (BigInteger) _times;
            if (bigTimes.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Multiplier have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigTimes.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Multiplier have to be greater or equals -2^31.", callFileName, callLine);
            times = bigTimes.intValue();
        }
        return parent.multiply(times);
    }
}
