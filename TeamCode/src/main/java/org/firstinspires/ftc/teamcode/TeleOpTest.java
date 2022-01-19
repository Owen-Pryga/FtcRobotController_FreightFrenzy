package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "TestOp")
public class TeleOpTest extends LinearOpMode{
    private boolean buttonPressed;
    RobotDrive robot = new RobotDrive();
    public void runOpMode()
    {
        waitForStart();
        robot.initializeRobot(hardwareMap, telemetry, RobotDrive.allianceColor.blue);
        robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.AQUA);
        //drop the arm uppon initalization dont got to do it here just testing do it in auto
        robot.dropArm.setPosition(1);

        while (opModeIsActive())
    {


        double forward = gamepad1.left_stick_y * -1; //the gamepad is reversed thus the -1
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x * robot.motorPower;

        robot.mixDrive(forward, strafe, rotate);

        //up is left trigger aka moving the motor in the negative direction
        //set the power and direction of lift motor by subtracting the two values also check to see if robot is at top or bottom
        if(robot.liftMotor.getCurrentPosition() <= 4700)
            robot.liftMotor.setPower(gamepad2.right_trigger);
        else if(robot.dist.getDistance(DistanceUnit.INCH) <= 1.8)
            robot.liftMotor.setPower(-gamepad2.left_trigger);
        else if(!(robot.liftMotor.getCurrentPosition() <= -4700) || (robot.dist.getDistance(DistanceUnit.INCH) <= 1.8))
            robot.liftMotor.setPower(gamepad2.right_trigger - gamepad2.left_trigger);


        //if the robot arm is at the base position reset arm encoder to ensure accuracy
        if(robot.dist.getDistance(DistanceUnit.INCH) <= 1.8) {
            robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        //slow down the robot if right bumper is pressed
        if(gamepad1.right_bumper)
            robot.motorPower = 0.5;
        else
            robot.motorPower = 1;


        //turn on the duck wheel when gamepad2's a button is pressed
        if(gamepad2.a)
            robot.duckMotor.setPower(0.5);
        else
            robot.duckMotor.setPower(0);


        //toggle the block grabber arm either on or off
        if(gamepad2.x && robot.Grabber.getPosition() > 0.5 && buttonPressed == false) {
            robot.Grabber.setPosition(0);
            buttonPressed = true;
        } else if(gamepad2.x && robot.Grabber.getPosition() < 0.5 && buttonPressed == false) {
            robot.Grabber.setPosition(1);
            buttonPressed = true;
        }else if(!gamepad2.x)
            buttonPressed = false;




        telemetry.addData("Left Rear motor power: ",robot.motors[0].getPower());
        telemetry.addData("GrabberServo: ", robot.Grabber.getPosition());
        telemetry.addData("Distance: ","%.3f",((DistanceSensor) robot.dist).getDistance(DistanceUnit.INCH));
        telemetry.addData("LiftEncoder",robot.liftMotor.getCurrentPosition());
        telemetry.update();
    }
    }
}
