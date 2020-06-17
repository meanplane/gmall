import http from '@/utils/httpRequest.js'
import S3 from 'aws-sdk/clients/s3'

export function policy () {
  return new Promise((resolve, reject) => {
    http({
      url: http.adornUrl('/thirdparty/oss/policy'),
      method: 'get',
      params: http.adornParams({})
    }).then(({data}) => {
      resolve(data)
    })
  })
}

export function getSecret () {
  return new Promise(((resolve, reject) => {
    let uid = Math.floor(Math.random()*100000)+1000000
    http({
      url: http.adornUrl(`/product/aws/getsecret/${uid}`),
      method: 'get',
      params: http.adornParams({})
    }).then(({data}) => {
      resolve(data)
    })
  }))

}

let renameFile = (file) => {
  let name = file.name;
  let tmp = name.split('.');
  let ext = tmp[tmp.length - 1].toLowerCase();

  return `${Date.now()}.${ext}`
};

export function uploadFile (file,secretKey,accessKey) {
  let aws_bucket = 'wallet'
  let aws_url = 'https://static.jlpfcj.com'

  // init s3
  let s3 = new S3({
    credentials: {accessKeyId:accessKey,secretAccessKey:secretKey},
    endpoint: aws_url,
    region: 'us-east-1',
    s3ForcePathStyle: true,
    apiVersion: '2006-03-01'
  });

  let newName = `gmall/${renameFile(file)}`;
  let params = Object.assign({
    Bucket: aws_bucket,
    Key: newName,
    Body: file,
    ACL: 'public-read'
  });

  return new Promise((resolve, reject) => {
    s3.upload(params, (err, data) => {
      if (err) reject(err);
      resolve(data);
    });
  });


}
