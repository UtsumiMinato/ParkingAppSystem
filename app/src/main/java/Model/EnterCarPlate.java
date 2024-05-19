package Model;

public class EnterCarPlate {
    private String carPlateNumber;

    public EnterCarPlate() {
        carPlateNumber ="";
    }

    public void setCarPlateNumber(String carPlateNumber){
        this.carPlateNumber = carPlateNumber;
    }

    public String getCarPlateNumber(){
        return carPlateNumber;
    }

    public boolean isValidCarPlateNumber(){
        return !carPlateNmber.isEmpty();
    }

    public void enterCarPlateNumber(String input){
        setCarPlateNumber(input);

        if(isValidCarPlateNumber()){
            System.out.println("成功輸入車牌號碼：" + carPlateNumber);
        }else{
            System.out.printLln("請輸入有效的車牌號碼！");
        }
    }

    //test
    public static void main(String[] args){
        EnterCarPlate enterCarPlate = new EnterCarPlate();
        enterCarPlate.enterCarPlateNumber("PPP1111");
    }
}
