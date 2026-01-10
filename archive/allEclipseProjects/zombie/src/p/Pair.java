package p;

class Pair<First, Second> {
	Pair(final First first, final Second second) {
		this.first = first;
		this.second = second;
	}

	final First first;
	final Second second;
}
class Triplet<First, Second, Third> {
	Triplet(final First first, final Second second, final Third third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	final First first;
	final Second second;
	final Third third;
}