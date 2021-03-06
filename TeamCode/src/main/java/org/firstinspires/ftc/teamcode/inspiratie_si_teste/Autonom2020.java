/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.inspiratie_si_teste;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Pushbot: Autonom2020", group="Pushbot")
@Disabled
public class

Autonom2020 extends LinearOpMode {

    /* Declare OpMode members. */


   /* private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AStJnD3/////AAABmSbeJsgIn0i0lCVUWxTrdiJtlmoJSTgwubOWegTxmEdsFzJkBuin+7gNMqUApOj8XkwLsbgKdDM2VsiC5ttIAUxBasPjSgQ5NLOLbkEX8E5hSWmmO73F3SBXRKP43WSNSCYDNRQdC3ZuGaVNn/3Xt3K5P82/890LHtbxK1NJc+R8bGZEH2bCAly6e1xYGkTbfaGSHkvnIxtQQl3XstJL9Q96D9MSZEcbSjr8JYB5NsoOujufJRkxsIhtmpshfxzyHNs9Xo+4QlQL2AHj/F0NCYMfqOTk19C12o8jJ2YeQkHEib2OBHmVKMi+V/ptEAEeRTmkEHBNNew8j5sd0gNLJmTo/CXFy3f/Fp0ZBgM21dy2";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;*/


    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    private DcMotor stanga_f = null;
    private DcMotor dreapta_f = null;
    private DcMotor stanga_s = null;
    private DcMotor dreapta_s = null;
    private DcMotor motor_brat = null;
    private DcMotor motor_brat_colectare = null;
    private DcMotor motor_brat_aruncare=null;

    private Servo servo_aruncare = null;
    private Servo servo_cleste = null;

    private DistanceSensor sensorRange;


    @Override
    public void runOpMode() {


        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        stanga_f = hardwareMap.get(DcMotor.class, "stanga_f");
        stanga_s = hardwareMap.get(DcMotor.class, "stanga_s");
        dreapta_f = hardwareMap.get(DcMotor.class, "dreapta_f");
        dreapta_s = hardwareMap.get(DcMotor.class, "dreapta_s");

        motor_brat = hardwareMap.get(DcMotor.class, "motor_brat");
        motor_brat_aruncare = hardwareMap.get(DcMotor.class, "motor_brat_aruncare");
        motor_brat_colectare = hardwareMap.get(DcMotor.class, "motor_brat_colectare");

        servo_cleste = hardwareMap.get(Servo.class, "servo_cleste");

        servo_aruncare = hardwareMap.get(Servo.class, "servo_aruncare");

        stanga_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        stanga_s.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dreapta_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dreapta_s.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        stanga_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stanga_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        dreapta_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        dreapta_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        stanga_f.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stanga_s.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dreapta_f.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dreapta_s.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        stanga_f.setDirection(DcMotor.Direction.FORWARD);
        stanga_s.setDirection(DcMotor.Direction.FORWARD);
        dreapta_f.setDirection(DcMotor.Direction.REVERSE);
        dreapta_s.setDirection(DcMotor.Direction.REVERSE);

        motor_brat_aruncare.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor_brat_colectare.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor_brat.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Path0", "Starting at %7d :%7d",
                stanga_f.getCurrentPosition(),
                stanga_s.getCurrentPosition());
        telemetry.update();

        //initVuforia();

        /*if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
        else
            {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
         */
        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
       // if (tfod != null) {
           // tfod.activate();
        //}

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        int i;
        i = 1;
       // motor_brat.setPower(1);
       // while (i != 200000) {
            servo_cleste.setPosition(1);

            straif(1, -15, 0, 2);
            encoderDrive(0.5, 2, -2, 0.75);

            straif(1, 2, 0, 0.65);
            servo_cleste.setPosition(0);
            straif(1,-1,0,0.40);
            straif(0.5 ,0 ,5,1 );
            encoderDrive(0.5, -2, 2, 0.75);
           // straif(1,0,-2,0.40);

          /*  motor_brat.setPower(-0.7);
            while(i!=200) {
                 i++;
            }
            i=1;
             servo_cleste.setPosition(1);
             straif(0.50,1,0,0.40);
            while(i!=400)
            {
                i++;
            }
            straif(1,-10,0,0.40);
            encoderDrive(1, 2, -2, 0.55);
            servo_cleste.setPosition(0);
            straif(1,2,0,0.70);
*/

                telemetry.addData("Path", "Complete");
                telemetry.update();
            }


    public void straif(double speed, double forwardMovement, double lat,double timeoutS) {
        int newLeftTarget_f;
        int newLeftTarget_s;
        int newRightTarget_f;
        int newRightTarget_s;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget_f = stanga_f.getCurrentPosition() + (int) (forwardMovement * COUNTS_PER_INCH);
            newLeftTarget_s = stanga_s.getCurrentPosition() + (int) (forwardMovement * COUNTS_PER_INCH);
            newRightTarget_f = dreapta_f.getCurrentPosition() + (int) (forwardMovement * COUNTS_PER_INCH);
            newRightTarget_s = dreapta_s.getCurrentPosition() + (int) (forwardMovement * COUNTS_PER_INCH);

            int latLeftTarget_f = stanga_f.getCurrentPosition() + (int) (lat * COUNTS_PER_INCH);
            int latLeftTarget_s = stanga_s.getCurrentPosition() + (int) (lat * COUNTS_PER_INCH);
            int latRightTarget_f = dreapta_f.getCurrentPosition() + (int) (lat * COUNTS_PER_INCH);
            int latRightTarget_s = dreapta_s.getCurrentPosition() + (int) (lat * COUNTS_PER_INCH);

            stanga_f.setTargetPosition(newLeftTarget_f - latLeftTarget_f);
            stanga_s.setTargetPosition(newLeftTarget_s + latLeftTarget_s);
            dreapta_f.setTargetPosition(newRightTarget_f - latRightTarget_f);
            dreapta_s.setTargetPosition(newRightTarget_s + latRightTarget_s);
            // Turn On RUN_TO_POSITION
            stanga_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            stanga_s.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dreapta_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dreapta_s.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            stanga_f.setPower(Math.abs(speed));
            stanga_s.setPower(Math.abs(speed));
            dreapta_f.setPower(Math.abs(speed));
            dreapta_s.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (stanga_f.isBusy() && stanga_s.isBusy() && dreapta_f.isBusy() && dreapta_s.isBusy())) {

                telemetry.addData("Path1", "Running to %7d :%7d : %7d : %7d", newLeftTarget_f, newLeftTarget_s, newRightTarget_s, newRightTarget_f);
                telemetry.addData("Path2", "Running at %7d :%7d : %7d : %7d",
                        stanga_f.getCurrentPosition(),
                        stanga_s.getCurrentPosition(), dreapta_s.getCurrentPosition(), dreapta_f.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            stanga_f.setPower(0);
            stanga_s.setPower(0);
            dreapta_s.setPower(0);
            dreapta_f.setPower(0);

            stanga_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            stanga_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            dreapta_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            dreapta_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget_f;
        int newLeftTarget_s;
        int newRightTarget_f;
        int newRightTarget_s;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget_f = stanga_f.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newLeftTarget_s = stanga_s.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget_f = dreapta_f.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newRightTarget_s = dreapta_s.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            stanga_f.setTargetPosition(newLeftTarget_f);
            stanga_s.setTargetPosition(newLeftTarget_s);
            dreapta_f.setTargetPosition(newRightTarget_f);
            dreapta_s.setTargetPosition(newRightTarget_s);

            // Turn On RUN_TO_POSITION
            stanga_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            stanga_s.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dreapta_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dreapta_s.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            stanga_f.setPower(Math.abs(speed));
            stanga_s.setPower(Math.abs(speed));
            dreapta_f.setPower(Math.abs(speed));
            dreapta_s.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (stanga_f.isBusy() && stanga_s.isBusy() && dreapta_f.isBusy() && dreapta_s.isBusy())) {

                telemetry.addData("Path1", "Running to %7d :%7d : %7d : %7d", newLeftTarget_f, newLeftTarget_s, newRightTarget_s, newRightTarget_f);
                telemetry.addData("Path2", "Running at %7d :%7d : %7d : %7d",
                        stanga_f.getCurrentPosition(),
                        stanga_s.getCurrentPosition(), dreapta_s.getCurrentPosition(), dreapta_f.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            stanga_f.setPower(0);
            stanga_s.setPower(0);
            dreapta_s.setPower(0);
            stanga_f.setPower(0);

            stanga_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            stanga_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            dreapta_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            dreapta_s.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void setMotor_brat_colectare(double speed, double movement, double timeoutS) {
        int movement_do;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            movement_do = motor_brat_colectare.getCurrentPosition() + (int) (movement * COUNTS_PER_INCH);


            motor_brat_colectare.setTargetPosition(movement_do);


            // Turn On RUN_TO_POSITION
            motor_brat_colectare.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            motor_brat_colectare.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (motor_brat_colectare.isBusy())) {

                telemetry.addData("Path1", movement_do);
                telemetry.addData("Path2", motor_brat_colectare.getCurrentPosition());
            }

            // Stop all motion;
            motor_brat_colectare.setPower(0);

            motor_brat_colectare.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void setMotor_brat_aruncare(double speed, double movement, double timeoutS) {
        int movement_do;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            movement_do = motor_brat_aruncare.getCurrentPosition() + (int) (movement * COUNTS_PER_INCH);


            motor_brat_aruncare.setTargetPosition(movement_do);


            // Turn On RUN_TO_POSITION
            motor_brat_aruncare.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            motor_brat_aruncare.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (motor_brat_aruncare.isBusy())) {

                telemetry.addData("Path1", movement_do);
                telemetry.addData("Path2", motor_brat_aruncare.getCurrentPosition());
            }

            // Stop all motion;
            motor_brat_aruncare.setPower(0);

            motor_brat_aruncare.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void brat(double speed, double movement, double timeoutS) {
        int movement_do;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            movement_do = motor_brat.getCurrentPosition() + (int) (movement * COUNTS_PER_INCH);


            motor_brat.setTargetPosition(movement_do);


            // Turn On RUN_TO_POSITION
            motor_brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            motor_brat.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (motor_brat.isBusy())) {

                telemetry.addData("Path1", movement_do);
                //telemetry.addData("Path2", motor_cremaliera.getCurrentPosition());
            }

            // Stop all motion;
            motor_brat.setPower(0);

            motor_brat.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }


  /*  private void initVuforia() {

         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
       // vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

*/
    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
       // tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       // tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }


}
