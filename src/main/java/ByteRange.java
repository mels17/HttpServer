public class ByteRange {
    private int _start;
    private int _end;

    public ByteRange() {
        _start = 0;
        _end = 0;
    }

    public void setStart(int start) {
        _start = start;
    }

    public void setEnd(int end) {
        _end = end;
    }

    public int get_start() {
        return _start;
    }

    public int get_end() {
        return _end;
    }
}
