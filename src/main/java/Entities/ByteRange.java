package Entities;

public class ByteRange {
    private Long _start;
    private Long _end;

    public ByteRange() {
        _start = 0L;
        _end = 0L;
    }

    public ByteRange(Long _start, Long _end) {
        this._start = _start;
        this._end = _end;
    }

    public void setStart(Long start) {
        _start = start;
    }

    public void setEnd(Long end) {
        _end = end;
    }

    public Long get_start() {
        return _start;
    }

    public Long get_end() {
        return _end;
    }

    public ByteRange setStartAndEnd(Long start, Long end) {
        _start = start;
        _end = end;
        return this;
    }
}
