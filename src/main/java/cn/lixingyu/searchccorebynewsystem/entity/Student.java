package cn.lixingyu.searchccorebynewsystem.entity;

/**
 * @author lxxxxxxy
 * @time 2019/10/05 13:21
 */
public class Student {
    private String XH;
    private String XM;
    private String KCMC;
    private String CJ;
    private String XN;
    private String XQ;
    private String BZ;

    @Override
    public String toString() {
        return "Student{" +
                "XH='" + XH + '\'' +
                ", XM='" + XM + '\'' +
                ", KCMC='" + KCMC + '\'' +
                ", CJ='" + CJ + '\'' +
                ", XN='" + XN + '\'' +
                ", XQ='" + XQ + '\'' +
                ", BZ='" + BZ + '\'' +
                '}';
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getKCMC() {
        return KCMC;
    }

    public void setKCMC(String KCMC) {
        this.KCMC = KCMC;
    }

    public String getCJ() {
        return CJ;
    }

    public void setCJ(String CJ) {
        this.CJ = CJ;
    }

    public String getXN() {
        return XN;
    }

    public void setXN(String XN) {
        this.XN = XN;
    }

    public String getXQ() {
        return XQ;
    }

    public void setXQ(String XQ) {
        this.XQ = XQ;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public Student(String XH, String XM, String KCMC, String CJ, String XN, String XQ, String BZ) {
        this.XH = XH;
        this.XM = XM;
        this.KCMC = KCMC;
        this.CJ = CJ;
        this.XN = XN;
        this.XQ = XQ;
        this.BZ = BZ;
    }

    public Student() {
    }
}
