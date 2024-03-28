
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

class otpgenerator{
    
    private static ArrayList<TimeLimitedInteger> existingOTP=new ArrayList<>();
    
    
    private static final long TIMEOUT_DURATION=1*60*1000;  //3O min in milliseconds;
    

    public static void main(String[] args){

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> removeExpiredInteger(), 0, 1, TimeUnit.SECONDS);
        Scanner sc = new Scanner(System.in);

        boolean askloop=true;
        while(askloop){
            System.out.println("Want otp y/n?");
            String wantOTP=sc.nextLine();
            
            if(wantOTP.equals("y")){
                System.out.println("OTP for verification is- ");
                System.out.println(generateOTP());
                getExistingOTP();
            }else if(wantOTP.equals("n")){
                askloop=false;
            }else{

                System.out.println("Do you want otp y/n?");
            }
        }
        
    }
    
    public static void getExistingOTP(){
        Iterator<TimeLimitedInteger> iter=existingOTP.iterator();
        ArrayList<Integer> arr=new ArrayList<>();
        while(iter.hasNext()) {
            arr.add(iter.next().getValue());
        }
        System.out.println("Existing OTP:"+ arr.toString());
    }
    private static void addIntegerWithTimeout(int value){
        existingOTP.add(new TimeLimitedInteger(value, System.currentTimeMillis()));
    }
    
    public static int generateOTP(){
        Random rd=new Random();
        int min=100000;
        int max=1000000;

        int otp;
        otp=rd.nextInt(min, max);
        if(existingOTP.isEmpty()){
            addIntegerWithTimeout(otp);
            return otp;
        }else{
            while(existingOTP.contains(new TimeLimitedInteger(otp, 0))){
                otp=rd.nextInt(min, max);
            }
            addIntegerWithTimeout(otp);
            return otp;

        }

    }

    public static void removeExpiredInteger(){
        long currentTime=System.currentTimeMillis();
        Iterator<TimeLimitedInteger> iterator= existingOTP.iterator();
        while (iterator.hasNext()) {
            TimeLimitedInteger intValue=iterator.next();
            if(currentTime-intValue.getInsertionTime()>TIMEOUT_DURATION){
                iterator.remove();
                System.out.println("Removed element:"+ intValue.getValue());
            }
        }

    }


    static class TimeLimitedInteger{
        private int value;
        private long insertionTime;
        public TimeLimitedInteger(int value, long insertionTime){
            this.value=value;
            this.insertionTime=insertionTime;
        }

        public int getValue(){
            return value;
        }
        public long getInsertionTime(){
            return insertionTime;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TimeLimitedInteger other = (TimeLimitedInteger) obj;
            return this.value == other.value;
        }
    }
}