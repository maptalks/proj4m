package org.maptalks.proj4;

class Proj {

    private String projName;
    private String datumCode;

    String getProjName() {
        return projName;
    }

    void setProjName(String projName) {
        this.projName = projName;
    }

    String getDatumCode() {
        return datumCode;
    }

    void setDatumCode(String datumCode) {
        this.datumCode = datumCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Proj)) {
            return false;
        }
        Proj o = ((Proj) obj);
        return eql(this.projName, o.projName) && eql(this.datumCode, o.datumCode);
    }

    private boolean eql(Object a, Object b) {
        if (a != null) {
            return b != null && a.equals(b);
        } else {
            return b == null;
        }
    }
}
