package md.vnastasi.slv.data;

public sealed interface RangeSpec {

    record Line(int start, int end) implements RangeSpec {
    }

    record Time(String start, String end) implements RangeSpec {
    }
}
