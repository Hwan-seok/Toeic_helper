var nodemailer = require('nodemailer');
module.exports=function(sending,id,pass){
    const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'tttkhs96@gmail.com',
      pass: 'qw880918'
    }
  })
   const mailOptions = {
    from: 'tttkhs96@gmail.com',
    to: `${sending}`,
    subject: 'sended from node, finding id , password  ',
    text: `your id is ${id}, password is ${pass}` 
  }
}
