/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.command.Subsystem;
// import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.ActuatorDown;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * Add your docs here.
 */
public class Actuator extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public final static WPI_TalonSRX _liftFrontLeft = RobotMap._liftFrontLeft;
  public final static WPI_TalonSRX _liftBackLeft = RobotMap._liftBackLeft;
  public final static WPI_TalonSRX _liftFrontRight = RobotMap._liftFrontRight;
  public final static WPI_TalonSRX _liftBackRight = RobotMap._liftBackRight;
  public static boolean _endGame = RobotMap._endGame;

  // public final static ADXL345_SPI _gyro = RobotMap._gyro;

  public static double _lastCurrentFL;
  public static double _lastCurrentBL;
  public static double _lastCurrentFR;
  public static double _lastCurrentBR;
  public static double _thisCurrent;

  public static DigitalInput _fr_ul = new DigitalInput(0);
  public static DigitalInput _fr_ll = new DigitalInput(1);
  public static DigitalInput _br_ul = new DigitalInput(2);
  public static DigitalInput _br_ll = new DigitalInput(3);
  public static DigitalInput _bl_ul = new DigitalInput(4);
  public static DigitalInput _bl_ll = new DigitalInput(5);
  public static DigitalInput _fl_ul = new DigitalInput(6);
  public static DigitalInput _fl_ll = new DigitalInput(7);

  public static final double _fl_speed = 0.75;
  public static final double _fr_speed = 0.75;
  public static final double _bl_speed = 0.75;
  public static final double _br_speed = 0.75;

  public static boolean _fr_UP = false;
  public static boolean _fl_UP = false;
  public static boolean _bl_UP = false;
  public static boolean _br_UP = false;

  public static int testloop = 0;

  private final int _kTimeoutMs = 30;
  private final int _kPIDLoopIdx = 0;

  private final double _kp = 0.25;
  private final double _ki = 0.001;
  private final double _kd = 10;
  private final double _kf = 1023.0/7200.0;

  public static boolean cmd = false;


  //upperlimits 0,2,4,6
  //lowerlimits 1,3,5,7
  //FR, BR, BL, FL

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
   setDefaultCommand(new ActuatorDown());
  }

  public void ActuatorSetUp() {
    // _gyro.reset();
    _lastCurrentFL = 0.0;
    _lastCurrentBL = 0.0;
    _lastCurrentFR = 0.0;
    _lastCurrentBR = 0.0;
    _liftBackLeft.setInverted(true);
    _liftFrontLeft.setNeutralMode(NeutralMode.Brake);
    _liftBackLeft.setNeutralMode(NeutralMode.Brake);
    _liftFrontRight.setNeutralMode(NeutralMode.Brake);
    _liftBackRight.setNeutralMode(NeutralMode.Brake);

    _liftFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    _liftBackLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    _liftFrontRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    _liftBackRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

    _liftBackLeft.setSensorPhase(true);

    _liftFrontLeft.configNominalOutputForward(0,_kTimeoutMs);
    _liftFrontLeft.configNominalOutputReverse(0,_kTimeoutMs);
    _liftFrontLeft.configPeakOutputForward(1,_kTimeoutMs);
    _liftFrontLeft.configPeakOutputReverse(-1,_kTimeoutMs);

    _liftBackLeft.configNominalOutputForward(0,_kTimeoutMs);
    _liftBackLeft.configNominalOutputReverse(0,_kTimeoutMs);
    _liftBackLeft.configPeakOutputForward(1,_kTimeoutMs);
    _liftBackLeft.configPeakOutputReverse(-1,_kTimeoutMs);

    _liftFrontRight.configNominalOutputForward(0,_kTimeoutMs);
    _liftFrontRight.configNominalOutputReverse(0,_kTimeoutMs);
    _liftFrontRight.configPeakOutputForward(1,_kTimeoutMs);
    _liftFrontRight.configPeakOutputReverse(-1,_kTimeoutMs);

    _liftBackRight.configNominalOutputForward(0,_kTimeoutMs);
    _liftBackRight.configNominalOutputReverse(0,_kTimeoutMs);
    _liftBackRight.configPeakOutputForward(1,_kTimeoutMs);
    _liftBackRight.configPeakOutputReverse(-1,_kTimeoutMs);

    _liftFrontLeft.configAllowableClosedloopError(0, _kPIDLoopIdx, _kTimeoutMs);
    _liftBackLeft.configAllowableClosedloopError(0, _kPIDLoopIdx, _kTimeoutMs);
    _liftFrontRight.configAllowableClosedloopError(0, _kPIDLoopIdx, _kTimeoutMs);
    _liftBackRight.configAllowableClosedloopError(0, _kPIDLoopIdx, _kTimeoutMs);

    _liftFrontLeft.config_kF(_kPIDLoopIdx, _kf, _kTimeoutMs);
		_liftFrontLeft.config_kP(_kPIDLoopIdx, _kp, _kTimeoutMs);
		_liftFrontLeft.config_kI(_kPIDLoopIdx, _ki, _kTimeoutMs);
    _liftFrontLeft.config_kD(_kPIDLoopIdx, _kd, _kTimeoutMs);

    _liftBackLeft.config_kF(_kPIDLoopIdx, _kf, _kTimeoutMs);
		_liftBackLeft.config_kP(_kPIDLoopIdx, _kp, _kTimeoutMs);
		_liftBackLeft.config_kI(_kPIDLoopIdx, _ki, _kTimeoutMs);
    _liftBackLeft.config_kD(_kPIDLoopIdx, _kd, _kTimeoutMs);

    _liftFrontRight.config_kF(_kPIDLoopIdx, _kf, _kTimeoutMs);
		_liftFrontRight.config_kP(_kPIDLoopIdx, _kp, _kTimeoutMs);
		_liftFrontRight.config_kI(_kPIDLoopIdx, _ki, _kTimeoutMs);
    _liftFrontRight.config_kD(_kPIDLoopIdx, _kd, _kTimeoutMs);

    _liftBackRight.config_kF(_kPIDLoopIdx, _kf, _kTimeoutMs);
		_liftBackRight.config_kP(_kPIDLoopIdx, _kp, _kTimeoutMs);
		_liftBackRight.config_kI(_kPIDLoopIdx, _ki, _kTimeoutMs);
    _liftBackRight.config_kD(_kPIDLoopIdx, _kd, _kTimeoutMs);
  }

  public void Down() {
    
    /*
    if(Robot.m_oi.m_Controller1.getRawButton(3) == true) { //Pushes Front Two Down
      if(_fr_ll.get() == false) {
        _liftFrontRight.set(-_fr_speed);
      } else {
        _liftFrontRight.set(0);
      }
      if(_fl_ll.get() == false) {
        _liftFrontLeft.set(-_fl_speed);
      } else {
        _liftFrontLeft.set(0);
      }
    } else if(Robot.m_oi.m_Controller1.getRawButton(8) == true) { //Pulls Front 2 up and Pushes Back two down
      if(_fr_ll.get() == false) {
        _liftFrontRight.set(-_fr_speed);
      } else {
        _liftFrontRight.set(0);
      }
      if(_fl_ll.get() == false) {
        _liftFrontLeft.set(-_fl_speed);
      } else {
        _liftFrontLeft.set(0);
      }
      if(_br_ul.get() == false) {
        _liftBackRight.set(_br_speed);
      } else {
        _liftBackRight.set(0);
      }
      if(_bl_ul.get() == false) {
        _liftBackLeft.set(_bl_speed);
      } else {
        _liftBackLeft.set(0);
      }
    } else if(Robot.m_oi.m_Controller1.getRawButton(1)) { //Pulls Back Two Up
      if(_br_ll.get() == false) {
        _liftBackRight.set(-_br_speed);
      } else {
        _liftBackRight.set(0);
      }
      if(_bl_ll.get() == false) {
        _liftBackLeft.set(-_bl_speed);
      } else {
        _liftBackLeft.set(0);
      }
    } else if(Robot.m_oi.m_Controller1.getRawButton(9)) { // Pulls All Four Up
      if(_br_ll.get() == false) {
        _liftBackRight.set(-_br_speed);
      } else {
        _liftBackRight.set(0);
      }
      if(_bl_ll.get() == false) {
        _liftBackLeft.set(-_bl_speed);
      } else {
        _liftBackLeft.set(0);
      }

    }

    */

    //check the buttons measure the current.
    if((Robot.m_oi.m_Controller1.getRawButton(3) == true)) { // Back Down, X button
      if(_br_ul.get() == false){
        _liftBackRight.set(_br_speed);
        _lastCurrentBR = _liftBackRight.getOutputCurrent();
      }else{
        _liftBackRight.set(0);
      }
      // if(_br_ll.get() == false){
      //   _liftBackRight.set(_br_speed);
      //   _lastCurrentBR = _liftBackRight.getOutputCurrent();
      // }else{
      //   _liftBackRight.set(0);
      // }
      // if(_bl_ll.get() == false){
      //   _liftBackLeft.set(_bl_speed);
      // }else{
      //   _liftBackLeft.set(0.0);
      //   _lastCurrentBL = _liftBackLeft.getOutputCurrent();
      // }
      if(_bl_ul.get() == false){
        _liftBackLeft.set(_bl_speed);
        _lastCurrentBL = _liftBackLeft.getOutputCurrent();
      }else{
        _liftBackLeft.set(0.0);
      }

    }else if((Robot.m_oi.m_Controller1.getRawButton(1) == true)) { //All four down, A button
        _lastCurrentFL = _liftFrontLeft.getOutputCurrent();
        _lastCurrentBL = _liftBackLeft.getOutputCurrent();
        _lastCurrentFR = _liftFrontRight.getOutputCurrent();
        _lastCurrentBR = _liftBackRight.getOutputCurrent();

        // if(_fr_ll.get() == false){
        //   _liftFrontRight.set(-_fr_speed);
        // }else{
          // if(_liftBackRight.getOutputCurrent() > 15 /*&& _fr_UP == false*/){
          //   // _fr_UP =true;
          //   _liftBackRight.set(0);
          // }else{
          //   // if(_fr_UP == false){
          //     _liftBackRight.set(-_br_speed);
          //   // }else{
          //   //   _liftFrontRight.set(0);
          //   // }
          // }
        // }
        // if(_br_ul.get() == false){
        //   _liftBackRight.set(-_br_speed);
        // }else{
        //   if(_liftBackRight.getOutputCurrent() > 10 && _br_UP == false){
        //     _br_UP =true;
        //     _liftBackRight.set(0);
        //   }else{
        //     if(_br_UP == false){
        //       _liftBackRight.set(-_br_speed);
        //     }else{
        //       _liftBackRight.set(0);
        //     }
        //   }
        // }
        // if(_bl_ul.get() == false){
        //   _liftBackLeft.set(-_bl_speed);
        // }else{
        //   if(_liftBackLeft.getOutputCurrent() > 10 && _bl_UP == false){
        //     _bl_UP =true;
        //     _liftBackLeft.set(0);
        //   }else{
        //     if(_bl_UP == false){
        //       _liftBackLeft.set(-_bl_speed);
        //     }else{
        //       _liftBackLeft.set(0);
        //     }
        //   }
        // }
        // if(_fl_ll.get() == false){
        //   _liftFrontLeft.set(-_fl_speed);
        // }else{
          // if(_liftBackLeft.getOutputCurrent() > 15 /*&& _fl_UP == false*/){
          //   // _fl_UP =true;
          //   _liftBackLeft.set(0);
          // }else{
          //   // if(_fl_UP == false){
          //     _liftBackLeft.set(-_bl_speed);
          //   // }else{
          //   //   _liftFrontLeft.set(0);
          //   // }
          // }
        // }
        // if(_br_ul.get() == false){
        //   _liftBackRight.set(_br_speed);
        //   _lastCurrentBR = _liftBackRight.getOutputCurrent();
        // }else{
        //   _liftBackRight.set(0);
        // }
        // if(_bl_ul.get() == false){
        //   _liftBackLeft.set(_bl_speed);
        //   _lastCurrentBL = _liftBackLeft.getOutputCurrent();
        // }else{
        //   _liftBackLeft.set(0);
        // }
        if(_fr_ul.get() == false){
          _liftFrontRight.set(_fr_speed);
          _lastCurrentFR = _liftFrontRight.getOutputCurrent();
        }else{
          _liftFrontRight.set(0);
        }
        if(_fl_ul.get() == false){
          _liftFrontLeft.set(_fl_speed);
          _lastCurrentFL = _liftFrontLeft.getOutputCurrent();
        }else{
          _liftFrontLeft.set(0);
        }
        if(_bl_ul.get() == false){
          _liftBackLeft.set(_bl_speed);
          _lastCurrentBL = _liftBackLeft.getOutputCurrent();
        }else{
          _liftBackLeft.set(0.0);
        }
        if(_br_ul.get() == false){
          _liftBackRight.set(_br_speed);
          _lastCurrentBR = _liftBackRight.getOutputCurrent();
        }else{
          _liftBackRight.set(0);
        }

    // } else if ((Robot.m_oi.m_Controller1.getRawButton(8) == false) && (Robot.m_oi.m_Joystick2.getRawButton(7) ==true) && (Robot.m_oi.m_Joystick2.getRawButton(8) == true)){
      // _liftFrontLeft.configPeakCurrentLimit((int)_lastCurrentFL +1);
      // _liftBackLeft.configPeakCurrentLimit((int)_lastCurrentBL +1);
      // _liftFrontRight.configPeakCurrentLimit((int)_lastCurrentFR +1);
      // _liftBackRight.configPeakCurrentLimit((int)_lastCurrentBR +1);
      // _liftFrontLeft.configContinuousCurrentLimit((int)_lastCurrentFL);
      // _liftBackLeft.configContinuousCurrentLimit((int)_lastCurrentFR);
      // _liftFrontRight.configContinuousCurrentLimit((int)_lastCurrentBL);
      // _liftBackRight.configContinuousCurrentLimit((int)_lastCurrentBR);
      // _liftFrontLeft.enableCurrentLimit(true);
      // _liftBackLeft.enableCurrentLimit(true);
      // _liftFrontRight.enableCurrentLimit(true);
      // _liftBackRight.enableCurrentLimit(true);
    } else if (Robot.m_oi.m_Controller1.getRawButton(2) == true) {  // Back comes up, B button
      /*
      if(_br_ul.get() == false){
        _liftBackRight.set(-_br_speed);
      }else{
        if(_liftBackRight.getOutputCurrent() > 20 && _br_UP == false){
          _br_UP =true;
          _liftBackRight.set(0);
        }else{
          if(_br_UP == false){
            _liftBackRight.set(-_br_speed);
          }else{
            _liftBackRight.set(0);
          }
        }
    }
      if(_bl_ul.get() == false){
        _liftBackLeft.set(-_bl_speed);
      }else{
        _liftBackLeft.set(0.0);
      } // Front pushes down
      if(_fr_ll.get() == false){
        _liftFrontRight.set(0.51);
        _lastCurrentFR = _liftFrontRight.getOutputCurrent();
      }else{
        _liftFrontRight.set(0);
      }
      */
      // if(_br_ll.get() == false){
      //   _liftBackRight.set(-_br_speed);
      // }else{
        if(_liftBackRight.getOutputCurrent() > 15 /*&& _br_UP == false*/){
          // _br_UP =true;
          _liftBackRight.set(0);
        }else{
          // if(_br_UP == false){
            _liftBackRight.set(-_br_speed);
          // }else{
          //   _liftBackRight.set(0);
          // }
        }
      // }


      // if(_bl_ll.get() == false){
      //   _liftBackLeft.set(-_bl_speed);
      // }else{
        if(_liftBackLeft.getOutputCurrent() > 15 /*&& _bl_UP == false*/){
          // _bl_UP =true;
          _liftBackLeft.set(0);
        }else{
          // if(_bl_UP == false){
            _liftBackLeft.set(-_bl_speed);
          // }else{
          //   _liftBackLeft.set(0);
          // }
        }
      // }
      if(_fr_ul.get() == false){
        _liftFrontRight.set(_fr_speed);
        _lastCurrentFR = _liftFrontRight.getOutputCurrent();
      }else{
        _liftFrontRight.set(0);
      }
      if(_fl_ul.get() == false){
        _liftFrontLeft.set(_fl_speed);
        _lastCurrentFL = _liftFrontLeft.getOutputCurrent();
      }else{
        _liftFrontLeft.set(0);
      }
      
    } else if(Robot.m_oi.m_Controller2.getRawButton(4) == true){
      if(_liftFrontRight.getOutputCurrent() > 15 /*&& _fr_UP == false*/){
        _liftFrontRight.set(0);
      }else{
        _liftFrontRight.set(-_fr_speed);
      }
      if(_liftFrontLeft.getOutputCurrent() > 15 /*&& _fl_UP == false*/){
        // _fl_UP =true;
        _liftFrontLeft.set(0);
      }else{
          _liftFrontLeft.set(-_fl_speed);
      }

    } else if (Robot.m_oi.m_Controller1.getRawButton(8) == true) { //All up, Start button
      /*
      if(_fl_ul.get() == false){
        _liftFrontLeft.set(-_fl_speed);
      }else{
        if(_liftFrontLeft.getOutputCurrent() > 10 || _fl_UP == false){
          _fl_UP =true;
          _liftFrontLeft.set(0);
        }else{
          if(_fr_UP == false){
            _liftFrontLeft.set(-_fl_speed);
          }else{
            _liftFrontLeft.set(0);
          }
        }
      }
      if(_fr_ul.get() == false){
        _liftFrontRight.set(-_fr_speed);
      }else{
        if(_liftFrontLeft.getOutputCurrent() > 10 && _fl_UP == false){
          _fl_UP =true;
          _liftFrontLeft.set(0);
        }else{
          if(_fl_UP == false){
            _liftFrontLeft.set(-_fl_speed);
          }else{
            _liftFrontLeft.set(0);
          }
        }
      }
      */

      // if(_br_ll.get() == false){
      //   _liftBackRight.set(-_br_speed);
      // }else{
        if(_liftBackRight.getOutputCurrent() > 15 /*&& _br_UP == false*/){
          // _br_UP =true;
          _liftBackRight.set(0);
        }else{
          // if(_br_UP == false){
            _liftBackRight.set(-_br_speed);
          // }else{
          //   _liftBackRight.set(0);
          // }
        }
      // }

      // if(_bl_ll.get() == false){
      //   _liftBackLeft.set(-_bl_speed);
      // }else{
        if(_liftBackLeft.getOutputCurrent() > 15 /*&& _bl_UP == false*/){
          // _bl_UP =true;
          _liftBackLeft.set(0);
        }else{
          // if(_bl_UP == false){
            _liftBackLeft.set(-_bl_speed);
          // }else{
          //   _liftBackLeft.set(0);
          // }
        }
      // }


      // if(_fr_ll.get() == false){
      //   _liftFrontRight.set(-_fr_speed);
      // }else{
        if(_liftFrontRight.getOutputCurrent() > 15 /*&& _fr_UP == false*/){
          // _fr_UP =true;
          _liftFrontRight.set(0);
        }else{
          // if(_fr_UP == false){
            _liftFrontRight.set(-_fr_speed);
          // }else{
          //   _liftFrontRight.set(0);
          // }
        }
      // }


      // if(_fl_ll.get() == false){
      //   _liftFrontLeft.set(-_fl_speed);
      // }else{
        if(_liftFrontLeft.getOutputCurrent() > 15 /*&& _fl_UP == false*/){
          // _fl_UP =true;
          _liftFrontLeft.set(0);
        }else{
          // if(_fl_UP == false){
            _liftFrontLeft.set(-_fl_speed);
          // }else{
          //   _liftFrontLeft.set(0);
          // }
        }
      // }



      


     } else { 
       _liftFrontLeft.set(0);
      _liftBackLeft.set(0);
      _liftFrontRight.set(0);
      _liftBackRight.set(0);
      // _lastCurrentFL = 0.0;
      // _lastCurrentBL = 0.0;
      // _lastCurrentFR = 0.0;
      // _lastCurrentBR = 0.0;
      _liftFrontLeft.enableCurrentLimit(false);
      _liftBackLeft.enableCurrentLimit(false);
      _liftFrontRight.enableCurrentLimit(false);
      _liftBackRight.enableCurrentLimit(false);
    }
    
    if(testloop++ > 25){
      testloop =0;
      SmartDashboard.putBoolean("Limit FR UL", _fr_ul.get());
      // SmartDashboard.putBoolean("Limit FR LL", _fr_ll.get());
      SmartDashboard.putBoolean("Limit BR UL", _br_ul.get());
      // SmartDashboard.putBoolean("Limit BR LL", _br_ll.get());
      SmartDashboard.putBoolean("Limit BL UL", _bl_ul.get());
      // SmartDashboard.putBoolean("Limit BL LL", _bl_ll.get());
      SmartDashboard.putBoolean("Limit FL UL", _fl_ul.get());
      // SmartDashboard.putBoolean("Limit FL LL", _fl_ll.get());
      // SmartDashboard.putNumber("FR LAST Current",_lastCurrentFR);
      // SmartDashboard.putNumber("BR LAST Current",_lastCurrentBL);
      // SmartDashboard.putNumber("BL LAST Current",_lastCurrentBR);
      // SmartDashboard.putNumber("FL LAST Current",_lastCurrentFL);
  
      // SmartDashboard.putNumber("FR Current",_liftFrontRight.getOutputCurrent());
      // SmartDashboard.putNumber("BR Current",_liftBackRight.getOutputCurrent());
      // SmartDashboard.putNumber("BL Current",_liftBackLeft.getOutputCurrent());
      // SmartDashboard.putNumber("FL Current",_liftFrontLeft.getOutputCurrent());
  
      // SmartDashboard.putBoolean("END GAME FL UP", _fl_UP);
      // SmartDashboard.putBoolean("END GAME FR UP", _fr_UP);
      // SmartDashboard.putBoolean("END GAME BL UP", _bl_UP);
      // SmartDashboard.putBoolean("END GAME BR UP", _br_UP);
  
      // SmartDashboard.putNumber("Gyro x", _gyro.)
  
    }

    // if(Robot.m_oi.m_Controller3.getRawButton(1)){
    //   _liftBackLeft.set(ControlMode.PercentOutput, 0.3);
    //   cmd = true;
    // }
    // if(Robot.m_oi.m_Controller3.getRawButton(2)){
    //   _liftBackRight.set(ControlMode.PercentOutput, 0.3);
    //   cmd = true;
    // }
    // if(Robot.m_oi.m_Controller3.getRawButton(3)){
    //   _liftBackLeft.set(ControlMode.PercentOutput, -0.3);
    //   cmd = true;
    // }
    // if(Robot.m_oi.m_Controller3.getRawButton(4)){
    //   _liftBackRight.set(ControlMode.PercentOutput, -0.3);
    //   cmd = true;
    // }
    if(Robot.m_oi.m_Controller3.getRawButton(5)){
      cmd = true;
      _endGame = true;
      if(_liftFrontLeft.getSelectedSensorPosition() > 0){
        _liftFrontLeft.set(ControlMode.PercentOutput, -0.3);
      }else{
        _liftFrontLeft.set(ControlMode.PercentOutput, 0);
      }
      if(_liftBackLeft.getSelectedSensorPosition() > 0){
        _liftBackLeft.set(ControlMode.PercentOutput, -0.3);
      }else{
        _liftBackLeft.set(ControlMode.PercentOutput, 0);
      }
      if(_liftFrontRight.getSelectedSensorPosition() > 0){
        _liftFrontRight.set(ControlMode.PercentOutput, -0.3);
      }else{
        _liftFrontRight.set(ControlMode.PercentOutput, 0);
      }
      if(_liftBackRight.getSelectedSensorPosition() > 0){
        _liftBackRight.set(ControlMode.PercentOutput, -0.3);
      }else{
        _liftBackRight.set(ControlMode.PercentOutput, 0);
      }
    }

    if(Robot.m_oi.m_Controller1.getPOV() == 270){
      cmd = true;
      _endGame = true;
      if(_liftFrontLeft.getSelectedSensorPosition() < 146000)
      {
        _liftFrontLeft.set(ControlMode.PercentOutput,0.3);
      }else{
        _liftFrontLeft.set(ControlMode.PercentOutput,0);
      }
      if(_liftBackLeft.getSelectedSensorPosition() < 146000)
      {
        _liftBackLeft.set(ControlMode.PercentOutput,0.3);
      }else{
        _liftBackLeft.set(ControlMode.PercentOutput,0);
      }
      if(_liftFrontRight.getSelectedSensorPosition() < 146000)
      {
        _liftFrontRight.set(ControlMode.PercentOutput,0.3);
      }else{
        _liftFrontRight.set(ControlMode.PercentOutput,0);
      }
      if(_liftBackRight.getSelectedSensorPosition() < 146000)
      {
        _liftBackRight.set(ControlMode.PercentOutput,0.3);
      }else{
        _liftBackRight.set(ControlMode.PercentOutput,0);
      }
    }

    if(Robot.m_oi.m_Controller1.getPOV() == 180){
      cmd = true;
      _endGame = true;
      if(_liftFrontLeft.getSelectedSensorPosition() < 100000)
      {
        _liftFrontLeft.set(ControlMode.Velocity,8000);
      }else if(_liftFrontLeft.getSelectedSensorPosition() < 153000){
        _liftFrontLeft.set(ControlMode.Velocity,4000);
      }else{
        _liftFrontLeft.set(ControlMode.PercentOutput,0);
      }
      if(_liftBackLeft.getSelectedSensorPosition() < 100000)
      {
        _liftBackLeft.set(ControlMode.Velocity,8000);
      }else if(_liftBackLeft.getSelectedSensorPosition() < 153000){
        _liftBackLeft.set(ControlMode.Velocity,4000);
      }else{
        _liftBackLeft.set(ControlMode.PercentOutput,0);
      }
      if(_liftFrontRight.getSelectedSensorPosition() < 100000)
      {
        _liftFrontRight.set(ControlMode.Velocity,8000);
      }else if(_liftFrontRight.getSelectedSensorPosition() < 153000){
        _liftFrontRight.set(ControlMode.Velocity,4000);
      }else{
        _liftFrontRight.set(ControlMode.PercentOutput,0);
      }
      if(_liftBackRight.getSelectedSensorPosition() < 100000)
      {
        _liftBackRight.set(ControlMode.Velocity,8000);
      }else if(_liftBackRight.getSelectedSensorPosition() < 153000){
        _liftBackRight.set(ControlMode.Velocity,4000);
      }else{
        _liftBackRight.set(ControlMode.PercentOutput,0);
      }
    }

    if(Robot.m_oi.m_Controller1.getPOV() == 90){
      cmd = true;
      _endGame = true;
      if(_liftFrontLeft.getSelectedSensorPosition() < 100000)
      {
        _liftFrontLeft.set(ControlMode.Velocity,8000);
      }else if(_liftFrontLeft.getSelectedSensorPosition() < 153000){
        _liftFrontLeft.set(ControlMode.Velocity,4000);
      }else{
        _liftFrontLeft.set(ControlMode.PercentOutput,0);
      }

      if(_liftBackLeft.getSelectedSensorPosition() > 0){
        _liftBackLeft.set(ControlMode.PercentOutput, -1.0);
      }else{
        _liftBackLeft.set(ControlMode.PercentOutput, 0);
      }

      if(_liftFrontRight.getSelectedSensorPosition() < 100000)
      {
        _liftFrontRight.set(ControlMode.Velocity,8000);
      }else if(_liftFrontRight.getSelectedSensorPosition() < 153000){
        _liftFrontRight.set(ControlMode.Velocity,4000);
      }else{
        _liftFrontRight.set(ControlMode.PercentOutput,0);
      }
      if(_liftBackRight.getSelectedSensorPosition() > 0){
        _liftBackRight.set(ControlMode.PercentOutput, -1.0);
      }else{
        _liftBackRight.set(ControlMode.PercentOutput, 0);
      }
    }

    if(cmd = false){
      _liftFrontLeft.set(ControlMode.PercentOutput,0);
      _liftBackLeft.set(ControlMode.PercentOutput,0);
      _liftFrontRight.set(ControlMode.PercentOutput,0);
      _liftBackRight.set(ControlMode.PercentOutput,0);
    }

    if(_fl_ul.get()){
      _liftFrontLeft.setSelectedSensorPosition(0);
    }

    if(_bl_ul.get()){
      _liftBackLeft.setSelectedSensorPosition(0);
    }

    if(_fr_ul.get()){
      _liftFrontRight.setSelectedSensorPosition(0);
    }

    if(_br_ul.get()){
      _liftBackRight.setSelectedSensorPosition(0);
    }

    SmartDashboard.putNumber("Front Left Encoder", _liftFrontLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("Back Left Encoder", _liftBackLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("Front Right Encoder", _liftFrontRight.getSelectedSensorPosition());
    SmartDashboard.putNumber("Back Right Encoder", _liftBackRight.getSelectedSensorPosition());

  }

}
