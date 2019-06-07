func $eq this other;
    println this, other;
    if !(other instanceof List) | other.length() != this.length();
        return false;
    end;
    i = 0;
    for x in this;
        if x != other.get i;
            return false;
        end;
        i += 1;
    end;
    return true;
end;

func $neq this other -> !(this == other);

func $r_eq other this -> this == other;

func $r_neq other this -> this != other;

func $r_mul other this -> this * other;

func $r_a_mul other this -> this *= other;

func replace toBeReplaced replacement;
    i = 0;
    for x in This;
        if toBeReplaced == x;
           set(i, replacement);
        end;
        i += 1;
    end;
    return This;
end;

func find value;
    i = 0;
    for x in This;
        if value == x;
            return i;
        end;
        i += 1;
    end;
    return -1;
end;

func remove value;
    pop find value;
    return This;
end;

func count value;
    counter = 0;
    for x in This;
        if value == x;
            counter += 1;
        end;
    end;
    return counter;
end;

func sublist first last;
    result = [Nothing] * (last - first + 1);
    i = 0;
    while first <= last;
        result.set(i, get first);
        first += 1;
        i += 1;
    end;
    return result;
end;

struct Iterator;
    counter = 0;
    func hasNext -> counter < length();

    func next;
        result = get counter;
        counter += 1;
        return result;
    end;
end;