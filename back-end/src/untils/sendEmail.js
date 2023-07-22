const nodemailer = require("nodemailer");

const sendEmail = (subject, message, send_to, send_from, reply_to) => {
  //create email transporter (định dang email)
    const transporter = nodemailer.createTransport({
      service: process.env.EMAIL_HOST,
      auth: {
        user: process.env.EMAIL_USERNAME,
        password: process.env.EMAIL_PASSWORD,
      },
      tls: {
        rejectUnauthorized: false,
      },
    });
    // Option for sending email
    const options = {
      from: send_from,
      to: send_to,
      replyTo: reply_to,
      subject: subject,
      html: message,
    };
    console.log(options)
    // send email
    transporter.sendMail(options,(err,info)=>{
      if(err){
        console.log(err)
      }else{
        console.log("email sent: ",info)

      }

    })
  
};

module.exports = sendEmail;
