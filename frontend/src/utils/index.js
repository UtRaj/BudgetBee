import bcrypt from "bcryptjs";

export const encryptPassword = (password) => {
  const saltCount = 15;
  return bcrypt.hashSync(password, saltCount);
};
