package org.firstinspires.ftc.teamcode.robots.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.robots.constatns.UniversalValues;
import org.firstinspires.ftc.teamcode.robots.subSystems.GlisieraSubsystem;
import org.firstinspires.ftc.teamcode.robots.subSystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.robots.subSystems.ScoringSubsystem;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class autoBlueClose extends LinearOpMode {
    HSVPipelineAuto pipeline;
    SampleMecanumDrive drive;
    ScoringSubsystem outake;
    IntakeSubsystem intake;
    GlisieraSubsystem glisiera;
    OpenCvCamera camera;
    int caz = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        pipeline = new HSVPipelineAuto(2);
        // initOpenCV();
        drive = new SampleMecanumDrive(hardwareMap);
        outake = new ScoringSubsystem(hardwareMap);
        glisiera = new GlisieraSubsystem(hardwareMap, this.telemetry);
        intake = new IntakeSubsystem(hardwareMap);

        Trajectory toMovCenter = drive.trajectoryBuilder(new Pose2d(0, 0, Math.toRadians(0)))
                .lineToConstantHeading(new Vector2d(31, 0)).build();
        Trajectory backCenter = drive.trajectoryBuilder(toMovCenter.end())
                .lineToLinearHeading(new Pose2d(20, 0, Math.toRadians(0)))
                .build();
        Trajectory centerToBackBoard = drive.trajectoryBuilder(backCenter.end())
                .lineToLinearHeading(new Pose2d(30, 40, Math.toRadians(-90)))
                .build();
        TrajectorySequence centerToStackMid = drive.trajectorySequenceBuilder(centerToBackBoard.end())
                .lineToLinearHeading(new Pose2d(70, -10, Math.toRadians(-90)))
                .build();
        TrajectorySequence centerStackToBackbordMid1 = drive.trajectorySequenceBuilder(centerToStackMid.end())
                .setTangent(0)
                .lineToLinearHeading(new Pose2d(28, 40, Math.toRadians(-90)))
                .build();
        Trajectory toMovFar = drive.trajectoryBuilder(new Pose2d(0,0,Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(30, -9, Math.toRadians(-90)))
                .build();
        Trajectory toBackboardFar = drive.trajectoryBuilder(toMovFar.end())
                .lineToLinearHeading(new Pose2d(35, 40, Math.toRadians(-90)))
                .build();
        Trajectory toMovClose = drive.trajectoryBuilder(new Pose2d(0,0, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(30, +10, Math.toRadians(0)))
                .build();
        Trajectory toBackboardClose = drive.trajectoryBuilder(toMovClose.end())
                .lineToLinearHeading(new Pose2d(20, +10, Math.toRadians(0)))
                .build();
        Trajectory toBackboardClose2 = drive.trajectoryBuilder(toBackboardClose.end())
                .lineToLinearHeading(new Pose2d(30, +45,  Math.toRadians(-90)))
                .build();
        outake.setPressure(UniversalValues.pressure_close);
        outake.setBrat(0.15);
        outake.setPivot(UniversalValues.pivotJos);
        outake.setTiwst(UniversalValues.twistDef);
        intake.setDropdown(UniversalValues.DROPDOWN_UP);
        intake.setStackBlocker(0);
//        while(!opModeIsActive() && !isStopRequested()) {
//            caz = pipeline.getCaz();
//            telemetry.addData("caz", caz);
//            telemetry.update();
//        }
        waitForStart();
        if(caz == 1) {
            drive.followTrajectory(toMovCenter);
            intake.setIntakePower(0.2);
            sleep(500);
            drive.followTrajectory(backCenter);
            drive.followTrajectory(centerToBackBoard);
            outake.setBrat(UniversalValues.bratIntermediary);
            sleep(200);
            intake.setIntakePos(1000);
            outake.setPivot(UniversalValues.pivotIntermediary);
            glisiera.setPosition(100);
            sleep(500);
            outake.setBrat(UniversalValues.bratSus);
            outake.setPivot(UniversalValues.pivotSus);
            outake.setTiwst(UniversalValues.twistScore);
            sleep(2000);
            outake.setPressure(UniversalValues.pressure_open);
            sleep(400);
            outake.setPivot(UniversalValues.pivotJos);
            outake.setBrat(0.15);
            outake.setTiwst(UniversalValues.twistDef);
            intake.setIntakePos(500);
            sleep(600);
            glisiera.setPosition(UniversalValues.GLISIERA_DEFAULT);
            outake.setBrat(UniversalValues.bratJos);
            intake.setIntakePos(0);
            sleep(500);
            intake.setDropdown(0.4);
            drive.followTrajectorySequence(centerToStackMid);
            sleep(400);
            intake.setIntakePos(2500);
            intake.setIntakeMobilPower(UniversalValues.INTAKE_MOBIL_POW - 0.3);
            intake.setIntakePower(UniversalValues.INTAKE_POW);
            sleep(500);
            intake.setStackBlocker(0.2);
            sleep(2000);
            intake.setIntakePos(0);
            sleep(1000);
            outake.setPressure(UniversalValues.pressure_close);
            intake.setIntakeMobilPower(0);
            intake.setIntakePower(0);
            sleep(2000);
            drive.followTrajectorySequence(centerStackToBackbordMid1);
            sleep(30000);

        } else if(caz == 3) {
            intake.setIntakePower(0.3);
            drive.followTrajectory(toMovFar);
            intake.setIntakePower(0.3);sleep(500);
            drive.followTrajectory(toBackboardFar);
            outake.setBrat(UniversalValues.bratIntermediary);
            sleep(200);
            intake.setIntakePos(1000);
            outake.setPivot(UniversalValues.pivotIntermediary);
            glisiera.setPosition(100);
            sleep(500);
            outake.setBrat(UniversalValues.bratSus);
            outake.setPivot(UniversalValues.pivotSus);
            outake.setTiwst(UniversalValues.twistScore);
            sleep(2000);
            outake.setPressure(UniversalValues.pressure_open);
            sleep(400);
            outake.setPivot(UniversalValues.pivotJos);
            outake.setBrat(0.15);
            outake.setTiwst(UniversalValues.twistDef);
            intake.setIntakePos(500);
            sleep(600);
            glisiera.setPosition(UniversalValues.GLISIERA_DEFAULT);
            outake.setBrat(UniversalValues.bratJos);
            intake.setIntakePos(0);
            sleep(30000);
        } else if (caz == 2) {
            drive.followTrajectory(toMovClose);
            intake.setIntakePower(0.2);
            sleep(500);
            drive.followTrajectory(toBackboardClose);
            drive.followTrajectory(toBackboardClose2);
            outake.setBrat(UniversalValues.bratIntermediary);
            sleep(200);
            intake.setIntakePos(1000);
            outake.setPivot(UniversalValues.pivotIntermediary);
            glisiera.setPosition(100);
            sleep(500);
            outake.setBrat(UniversalValues.bratSus);
            outake.setPivot(UniversalValues.pivotSus);
            outake.setTiwst(UniversalValues.twistScore);
            sleep(2000);
            outake.setPressure(UniversalValues.pressure_open);
            sleep(400);
            outake.setPivot(UniversalValues.pivotJos);
            outake.setBrat(0.15);
            outake.setTiwst(UniversalValues.twistDef);
            intake.setIntakePos(500);
            sleep(600);
            glisiera.setPosition(UniversalValues.GLISIERA_DEFAULT);
            outake.setBrat(UniversalValues.bratJos);
            intake.setIntakePos(0);
            sleep(30000);
        }
    }
    private void initOpenCV() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        camera.setPipeline(pipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1280,720, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode)
            {}
        });
    }
}
