struct Iterable;
	var iterable;
	
	func init iterable;
		This.iterable = iterable;
	end;
	
	func get index;
		var counter = 0;
		for x in This;
			if counter index ==;
				return x;
			end;
			counter 1 +=;
		end;
		logError("Iterable doesn't have " +  index + " element");
	end;
	
	func toList;
		var list = [Nothing] * length(This);
		var i = 0;
		for x in This;
			list.set(i, x);
			i += 1;
		end;
		return list;
	end;
	
	struct Iterator;
		var iterator = iterable.Iterator();
		func next;
			return iterator.next();
		end;
		
		func hasNext;
			return iterator.hasNext();
		end;
	end;
end;